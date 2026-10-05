package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PropertyCategory(val displayName: String) {
    ALL("All Categories"),
    PLOT("Plots & Files"),
    HOUSE("Houses & Villas"),
    CONSTRUCTION("Turnkey Construction"),
    DESIGN("Architectural Design"),
    COMMERCIAL("Commercial")
}

enum class ListingPurpose(val displayName: String) {
    FOR_SALE("For Sale"),
    FOR_RENT("For Rent"),
    WANTED("Required / Wanted"),
    SERVICE("Builder Service")
}

enum class LahoreSociety(val displayName: String, val shortName: String) {
    ALL("All Lahore", "All"),
    VALENCIA("Valencia Town", "Valencia"),
    LAKE_CITY("Lake City", "Lake City"),
    DHA("DHA (All Phases)", "DHA Lahore"),
    BAHRIA("Bahria Town", "Bahria Town"),
    JOHAR("Johar Town", "Johar Town"),
    MODEL("Model Town", "Model Town"),
    WAPDA("Wapda Town", "Wapda Town"),
    ETIHAD("Etihad Town", "Etihad Town"),
    RAIWIND("Raiwind Road", "Raiwind Rd"),
    OTHER("Other Societies", "Other")
}

@Entity(tableName = "property_listings")
data class PropertyListing(
    @PrimaryKey val id: String,
    val title: String,
    val category: PropertyCategory,
    val purpose: ListingPurpose,
    val society: LahoreSociety,
    val sectorOrBlock: String,
    val size: String,
    val priceDisplay: String,
    val priceNumeric: Long, // in PKR
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val coveredAreaSqFt: Int = 0,
    val description: String,
    val features: String, // comma separated
    val builderTag: String = "Designer Homes Exclusive",
    val contactPhone: String = "03364251212",
    val isUserSubmitted: Boolean = false,
    val imageType: String = "house_modern", // "house_modern", "house_spanish", "plot", "commercial", "construction", "design"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "lead_submissions")
data class LeadSubmission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val leadType: String, // "POST_AD", "BUY_PLOT", "BUY_HOUSE", "CONSTRUCTION_INQUIRY", "DESIGN_CONSULTATION", "SITE_VISIT"
    val clientName: String,
    val clientPhone: String,
    val society: String,
    val propertyCategory: String,
    val size: String,
    val budgetOrDemand: String,
    val details: String,
    val isSentToWhatsApp: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_favorites")
data class SavedFavorite(
    @PrimaryKey val listingId: String,
    val savedAt: Long = System.currentTimeMillis()
)

data class ConstructionEstimate(
    val plotSize: String,
    val coveredAreaSqFt: Int,
    val packageType: String,
    val ratePerSqFt: Int,
    val totalCostPKR: Long,
    val grayStructureCost: Long,
    val finishingCost: Long,
    val architecturalDesignCost: Long,
    val estimatedDurationMonths: Int,
    val materialHighlights: List<String>
)
