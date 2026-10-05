package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ConstructionEstimate
import com.example.data.model.LahoreSociety
import com.example.data.model.LeadSubmission
import com.example.data.model.ListingPurpose
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyListing
import com.example.data.repository.PropertyRepository
import com.example.util.CostCalculator
import com.example.util.CostCalculator.ConstructionPackage
import com.example.util.WhatsAppLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PostAdFormState(
    val title: String = "",
    val category: PropertyCategory = PropertyCategory.HOUSE,
    val purpose: ListingPurpose = ListingPurpose.FOR_SALE,
    val society: LahoreSociety = LahoreSociety.VALENCIA,
    val sectorOrBlock: String = "",
    val size: String = "5 Marla",
    val demandPrice: String = "",
    val bedrooms: String = "3",
    val bathrooms: String = "4",
    val coveredAreaSqFt: String = "2250",
    val description: String = "",
    val features: String = "Near Park, Underground Electricity, Solid Construction",
    val clientName: String = "",
    val clientPhone: String = ""
)

class DesignerHomesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PropertyRepository

    init {
        val db = AppDatabase.getInstance(application)
        repository = PropertyRepository(db.propertyDao())
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }
    }

    // Navigation & Screen selection
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
        if (tabIndex != 0) {
            _selectedPropertyDetail.value = null
        }
    }

    // Detail Screen Selection
    private val _selectedPropertyDetail = MutableStateFlow<PropertyListing?>(null)
    val selectedPropertyDetail: StateFlow<PropertyListing?> = _selectedPropertyDetail.asStateFlow()

    fun openPropertyDetail(listing: PropertyListing) {
        _selectedPropertyDetail.value = listing
    }

    fun closePropertyDetail() {
        _selectedPropertyDetail.value = null
    }

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSocietyFilter = MutableStateFlow(LahoreSociety.ALL)
    val selectedSocietyFilter: StateFlow<LahoreSociety> = _selectedSocietyFilter.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow(PropertyCategory.ALL)
    val selectedCategoryFilter: StateFlow<PropertyCategory> = _selectedCategoryFilter.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSocietyFilter(society: LahoreSociety) {
        _selectedSocietyFilter.value = society
    }

    fun setCategoryFilter(category: PropertyCategory) {
        _selectedCategoryFilter.value = category
    }

    // Properties Data
    val allProperties: StateFlow<List<PropertyListing>> = repository.allProperties.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val favorites: StateFlow<Set<String>> = repository.allFavorites.combine(repository.allProperties) { favs, _ ->
        favs.map { it.listingId }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val filteredProperties: StateFlow<List<PropertyListing>> = combine(
        allProperties,
        _searchQuery,
        _selectedSocietyFilter,
        _selectedCategoryFilter
    ) { properties, query, society, category ->
        properties.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.title.contains(query, ignoreCase = true) ||
                    item.society.displayName.contains(query, ignoreCase = true) ||
                    item.sectorOrBlock.contains(query, ignoreCase = true) ||
                    item.size.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.features.contains(query, ignoreCase = true)

            val matchesSociety = (society == LahoreSociety.ALL) ||
                    (item.society == society) ||
                    (item.society == LahoreSociety.ALL)

            val matchesCategory = (category == PropertyCategory.ALL) || (item.category == category)

            matchesQuery && matchesSociety && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userLeads: StateFlow<List<LeadSubmission>> = repository.allLeads.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun toggleFavorite(listingId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(listingId)
        }
    }

    fun deleteProperty(id: String) {
        viewModelScope.launch {
            repository.deleteProperty(id)
        }
    }

    fun deleteLead(id: Long) {
        viewModelScope.launch {
            repository.deleteLead(id)
        }
    }

    // Lead Dialog & Quick Inquiry State
    private val _showQuickInquiryDialog = MutableStateFlow(false)
    val showQuickInquiryDialog: StateFlow<Boolean> = _showQuickInquiryDialog.asStateFlow()

    private val _quickInquiryPreset = MutableStateFlow<PropertyListing?>(null)
    val quickInquiryPreset: StateFlow<PropertyListing?> = _quickInquiryPreset.asStateFlow()

    fun openQuickInquiry(property: PropertyListing? = null) {
        _quickInquiryPreset.value = property
        _showQuickInquiryDialog.value = true
    }

    fun closeQuickInquiry() {
        _showQuickInquiryDialog.value = false
        _quickInquiryPreset.value = null
    }

    fun submitQuickInquiry(
        context: Context,
        name: String,
        phone: String,
        society: String,
        details: String,
        category: String = "Property Inquiry",
        size: String = ""
    ) {
        viewModelScope.launch {
            val lead = LeadSubmission(
                leadType = if (_quickInquiryPreset.value != null) "PROPERTY_INQUIRY" else "GENERAL_LEAD",
                clientName = name,
                clientPhone = phone,
                society = society,
                propertyCategory = category,
                size = size,
                budgetOrDemand = _quickInquiryPreset.value?.priceDisplay ?: "Negotiable",
                details = details,
                isSentToWhatsApp = true
            )
            repository.submitLead(lead)
            closeQuickInquiry()

            val msg = WhatsAppLauncher.formatLeadInquiryMessage(lead)
            WhatsAppLauncher.openWhatsApp(context, msg)
            Toast.makeText(context, "Lead saved & Opening WhatsApp for 03364251212", Toast.LENGTH_SHORT).show()
        }
    }

    // Post Ad Form State
    private val _postAdState = MutableStateFlow(PostAdFormState())
    val postAdState: StateFlow<PostAdFormState> = _postAdState.asStateFlow()

    fun updatePostAdForm(updater: (PostAdFormState) -> PostAdFormState) {
        _postAdState.value = updater(_postAdState.value)
    }

    fun resetPostAdForm() {
        _postAdState.value = PostAdFormState()
    }

    fun submitPostAd(context: Context) {
        val form = _postAdState.value
        if (form.title.isBlank()) {
            Toast.makeText(context, "Please enter property title", Toast.LENGTH_SHORT).show()
            return
        }
        if (form.demandPrice.isBlank()) {
            Toast.makeText(context, "Please enter demand or budget", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            val newId = "DH-USER-${System.currentTimeMillis() % 100000}"
            val newListing = PropertyListing(
                id = newId,
                title = form.title.trim(),
                category = form.category,
                purpose = form.purpose,
                society = form.society,
                sectorOrBlock = form.sectorOrBlock.ifBlank { "Lahore Prime Area" },
                size = form.size.trim(),
                priceDisplay = if (form.demandPrice.contains("PKR", ignoreCase = true)) form.demandPrice else "PKR ${form.demandPrice}",
                priceNumeric = parsePriceNumeric(form.demandPrice),
                bedrooms = form.bedrooms.toIntOrNull() ?: 0,
                bathrooms = form.bathrooms.toIntOrNull() ?: 0,
                coveredAreaSqFt = form.coveredAreaSqFt.toIntOrNull() ?: 0,
                description = form.description.ifBlank { "Prime property listed via Designer Homes Pakistan app." },
                features = form.features.ifBlank { "Clear Title, Direct Deal" },
                builderTag = "User Posted Ad",
                contactPhone = form.clientPhone.ifBlank { "03364251212" },
                isUserSubmitted = true,
                imageType = when (form.category) {
                    PropertyCategory.PLOT -> "plot"
                    PropertyCategory.COMMERCIAL -> "commercial"
                    PropertyCategory.CONSTRUCTION -> "construction"
                    PropertyCategory.DESIGN -> "design"
                    else -> "house_modern"
                }
            )

            // Save ad to listings
            repository.insertProperty(newListing)

            // Also record as a lead
            val lead = LeadSubmission(
                leadType = "POST_AD",
                clientName = form.clientName.ifBlank { "Ad Poster" },
                clientPhone = form.clientPhone.ifBlank { "03364251212" },
                society = form.society.displayName,
                propertyCategory = form.category.displayName,
                size = form.size,
                budgetOrDemand = form.demandPrice,
                details = "${form.title} - ${form.description}",
                isSentToWhatsApp = true
            )
            repository.submitLead(lead)

            // Open WhatsApp with formatted ad
            val message = WhatsAppLauncher.formatPostedAdMessage(newListing)
            WhatsAppLauncher.openWhatsApp(context, message)

            Toast.makeText(context, "Ad published! Forwarded to WhatsApp 03364251212", Toast.LENGTH_LONG).show()
            resetPostAdForm()
            _selectedTab.value = 0 // Return to listings
        }
    }

    // Construction Cost Calculator State
    private val _selectedPlotSize = MutableStateFlow("5 Marla")
    val selectedPlotSize: StateFlow<String> = _selectedPlotSize.asStateFlow()

    private val _customAreaSqFt = MutableStateFlow("")
    val customAreaSqFt: StateFlow<String> = _customAreaSqFt.asStateFlow()

    private val _selectedPackage = MutableStateFlow(ConstructionPackage.A_PLUS_TURNKEY)
    val selectedPackage: StateFlow<ConstructionPackage> = _selectedPackage.asStateFlow()

    private val _calcSociety = MutableStateFlow("Valencia Town")
    val calcSociety: StateFlow<String> = _calcSociety.asStateFlow()

    private val _calcClientName = MutableStateFlow("")
    val calcClientName: StateFlow<String> = _calcClientName.asStateFlow()

    private val _calcClientPhone = MutableStateFlow("")
    val calcClientPhone: StateFlow<String> = _calcClientPhone.asStateFlow()

    fun setPlotSize(size: String) {
        _selectedPlotSize.value = size
    }

    fun setCustomArea(sqFt: String) {
        _customAreaSqFt.value = sqFt
    }

    fun setConstructionPackage(pkg: ConstructionPackage) {
        _selectedPackage.value = pkg
    }

    fun setCalcSociety(society: String) {
        _calcSociety.value = society
    }

    fun setCalcClientName(name: String) {
        _calcClientName.value = name
    }

    fun setCalcClientPhone(phone: String) {
        _calcClientPhone.value = phone
    }

    val currentEstimate: StateFlow<ConstructionEstimate> = combine(
        _selectedPlotSize,
        _customAreaSqFt,
        _selectedPackage
    ) { size, customArea, pkg ->
        val area = customArea.toIntOrNull()
        CostCalculator.calculate(size, area, pkg)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CostCalculator.calculate("5 Marla", null, ConstructionPackage.A_PLUS_TURNKEY)
    )

    fun sendEstimateToWhatsApp(context: Context) {
        val estimate = currentEstimate.value
        val name = _calcClientName.value
        val phone = _calcClientPhone.value
        val society = _calcSociety.value

        viewModelScope.launch {
            val lead = LeadSubmission(
                leadType = "CONSTRUCTION_ESTIMATE",
                clientName = name.ifBlank { "Estimation Client" },
                clientPhone = phone.ifBlank { "WhatsApp User" },
                society = society,
                propertyCategory = estimate.packageType,
                size = "${estimate.plotSize} (~${estimate.coveredAreaSqFt} sq ft)",
                budgetOrDemand = "PKR ${estimate.totalCostPKR / 100000} Lacs",
                details = "Estimated rate: PKR ${estimate.ratePerSqFt}/sq ft. Duration: ${estimate.estimatedDurationMonths} months.",
                isSentToWhatsApp = true
            )
            repository.submitLead(lead)

            val msg = WhatsAppLauncher.formatConstructionEstimate(estimate, name, phone, society)
            WhatsAppLauncher.openWhatsApp(context, msg)
            Toast.makeText(context, "Opening WhatsApp for Builder Quotation (03364251212)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parsePriceNumeric(priceStr: String): Long {
        val clean = priceStr.lowercase().replace("pkr", "").replace(",", "").trim()
        return try {
            if (clean.contains("crore")) {
                val num = clean.replace("crore", "").trim().toDouble()
                (num * 10000000).toLong()
            } else if (clean.contains("lac")) {
                val num = clean.replace("lac", "").trim().toDouble()
                (num * 100000).toLong()
            } else {
                clean.filter { it.isDigit() }.toLongOrNull() ?: 0L
            }
        } catch (e: Exception) {
            0L
        }
    }
}
