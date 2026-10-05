package com.example.data.repository

import com.example.data.local.PropertyDao
import com.example.data.model.LahoreSociety
import com.example.data.model.LeadSubmission
import com.example.data.model.ListingPurpose
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyListing
import com.example.data.model.SavedFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PropertyRepository(private val propertyDao: PropertyDao) {

    val allProperties: Flow<List<PropertyListing>> = propertyDao.getAllProperties()
    val allLeads: Flow<List<LeadSubmission>> = propertyDao.getAllLeads()
    val allFavorites: Flow<List<SavedFavorite>> = propertyDao.getAllFavorites()

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        val currentList = propertyDao.getAllProperties().first()
        if (currentList.isEmpty()) {
            propertyDao.insertProperties(getSeedProperties())
        }
    }

    suspend fun insertProperty(property: PropertyListing) = withContext(Dispatchers.IO) {
        propertyDao.insertProperty(property)
    }

    suspend fun deleteProperty(id: String) = withContext(Dispatchers.IO) {
        propertyDao.deletePropertyById(id)
    }

    suspend fun submitLead(lead: LeadSubmission): Long = withContext(Dispatchers.IO) {
        propertyDao.insertLead(lead)
    }

    suspend fun deleteLead(id: Long) = withContext(Dispatchers.IO) {
        propertyDao.deleteLeadById(id)
    }

    suspend fun toggleFavorite(listingId: String) = withContext(Dispatchers.IO) {
        val favs = propertyDao.getAllFavorites().first()
        val exists = favs.any { it.listingId == listingId }
        if (exists) {
            propertyDao.removeFavorite(listingId)
        } else {
            propertyDao.addFavorite(SavedFavorite(listingId))
        }
    }

    fun isFavorite(listingId: String): Flow<Boolean> {
        return propertyDao.isFavorite(listingId)
    }

    private fun getSeedProperties(): List<PropertyListing> {
        return listOf(
            // VALENCIA TOWN
            PropertyListing(
                id = "DH-VAL-101",
                title = "10 Marla Ultra Modern Designer Spanish House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.VALENCIA,
                sectorOrBlock = "Block K (Near Central Commercial)",
                size = "10 Marla",
                priceDisplay = "PKR 3.85 Crore",
                priceNumeric = 38500000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 3600,
                description = "Brand new bespoke architectural masterpiece built with Grade 60 steel, imported Spanish porcelain tiles, solid ash wood doors, Grohe bathroom fittings, double height lobby, SMEG equipped kitchen with breakfast island, and private rooftop garden.",
                features = "5 Master Beds, Double Height Lobby, 2 Luxury Kitchens, Servant Quarter, Solar Inverter Installed, Valencia Commercial Access",
                builderTag = "Designer Homes Signature",
                imageType = "house_spanish"
            ),
            PropertyListing(
                id = "DH-VAL-102",
                title = "5 Marla Brand New Boutique Double Story Villa",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.VALENCIA,
                sectorOrBlock = "Block H (Near Main Park & Mosque)",
                size = "5 Marla",
                priceDisplay = "PKR 2.45 Crore",
                priceNumeric = 24500000L,
                bedrooms = 3,
                bathrooms = 4,
                coveredAreaSqFt = 2200,
                description = "Compact luxury designed for contemporary family living. Solid brick masonry, magnetic track lights, tempered glass balcony, and solid wood kitchen.",
                features = "3 En-suite Bedrooms, Stylish Drawing Room, Car Porch, High-grade UPVC Windows, Prime Valencia Location",
                builderTag = "Verified Construction",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-VAL-103",
                title = "1 Kanal Prime Direct Owner Residential Plot",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.VALENCIA,
                sectorOrBlock = "Sector D (Near Main Boulevard)",
                size = "1 Kanal",
                priceDisplay = "PKR 3.20 Crore",
                priceNumeric = 32000000L,
                description = "Ready for immediate construction of your dream mansion. 50 feet wide road facing, clear title with direct owner deal, utilities underground paid.",
                features = "50ft Wide Road, Solid Land No Depression, Utilities Ready, Possession & Transferable",
                builderTag = "Direct Owner Deal",
                imageType = "plot"
            ),

            // LAKE CITY LAHORE
            PropertyListing(
                id = "DH-LC-201",
                title = "10 Marla Golf Estate Luxury Contemporary Villa",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.LAKE_CITY,
                sectorOrBlock = "Sector M-3 (Golf Course Facing)",
                size = "10 Marla",
                priceDisplay = "PKR 4.90 Crore",
                priceNumeric = 49000000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 3800,
                description = "Stunning golf course facing luxury villa with expansive floor-to-ceiling glass windows, smart home automation, high-end imported marble and designer landscaped patio.",
                features = "Golf Course View, Smart Home Automation, Italian Botticino Marble, Double Terrace, Servant Room",
                builderTag = "Designer Homes Exclusive",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-LC-202",
                title = "5 Marla Brand New Modern House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.LAKE_CITY,
                sectorOrBlock = "Sector M-7 (Possession Ready)",
                size = "5 Marla",
                priceDisplay = "PKR 2.30 Crore",
                priceNumeric = 23000000L,
                bedrooms = 3,
                bathrooms = 4,
                coveredAreaSqFt = 2150,
                description = "Contemporary front elevation with Turkish rockwall textures, fall ceilings in all rooms, modular kitchen with imported acrylic cabinets.",
                features = "3 Beds with Attached Baths, Designer Powder Room, Spacious Rooftop, Near Ring Road Interchange",
                builderTag = "Hot Deal",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-LC-203",
                title = "5 Marla Developed Possession Plot",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.LAKE_CITY,
                sectorOrBlock = "Sector M-8 (Near Ring Road)",
                size = "5 Marla",
                priceDisplay = "PKR 95 Lac",
                priceNumeric = 9500000L,
                description = "Excellent investment and ready to construct plot in Lake City. High capital growth zone with immediate possession and LDA approval.",
                features = "Possession Ready, Underground Electricity, 1 min from Ring Road, Best ROI",
                builderTag = "Investment Opportunity",
                imageType = "plot"
            ),

            // DHA LAHORE (ALL PHASES)
            PropertyListing(
                id = "DH-DHA-301",
                title = "1 Kanal Ultra-Luxury Minimalist Mansion",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.DHA,
                sectorOrBlock = "Phase 6, Block H (Near Main Boulevard)",
                size = "1 Kanal",
                priceDisplay = "PKR 11.50 Crore",
                priceNumeric = 115000000L,
                bedrooms = 5,
                bathrooms = 7,
                coveredAreaSqFt = 5900,
                description = "Uncompromising architectural perfection. Featuring a central open-sky water courtyard, bespoke Spanish chandeliers, dual Italian kitchens, cinema room, elevator, and fully finished basement.",
                features = "Home Elevator, Swimming Pool / Courtyard, Cinema Room, Basement, 2 Imported Kitchens, Solar 20kW",
                builderTag = "Luxury Elite",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-DHA-302",
                title = "1 Kanal Spanish Classic Designer Villa",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.DHA,
                sectorOrBlock = "Phase 5, Block C (Prime Sector)",
                size = "1 Kanal",
                priceDisplay = "PKR 9.80 Crore",
                priceNumeric = 98000000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 5600,
                description = "Timeless Spanish architecture with handcrafted terracotta accents, majestic double height entrance foyer, master suites with walk-in closets and jacuzzi.",
                features = "Spanish Elevation, Jacuzzi Master Bath, Lush Front Lawn, 3 Car Porch, High Security Zone",
                builderTag = "Designer Homes Signature",
                imageType = "house_spanish"
            ),
            PropertyListing(
                id = "DH-DHA-303",
                title = "10 Marla Brand New Designer House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.DHA,
                sectorOrBlock = "Phase 8 (Air Avenue, Block L)",
                size = "10 Marla",
                priceDisplay = "PKR 5.75 Crore",
                priceNumeric = 57500000L,
                bedrooms = 4,
                bathrooms = 5,
                coveredAreaSqFt = 3700,
                description = "Modern cubical elevation with large thermal glass, solid teak wood doors, imported Grohe sanitary fittings and Italian tile work.",
                features = "4 Luxury Bedrooms, Near Commercial & Mosque, Imported Sanitary, Modern TV Lounge",
                builderTag = "Verified Listing",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-DHA-304",
                title = "1 Kanal Prime Corner Plot",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.DHA,
                sectorOrBlock = "Phase 9 Prism, Block D",
                size = "1 Kanal",
                priceDisplay = "PKR 2.85 Crore",
                priceNumeric = 28500000L,
                description = "Ideal corner plot facing park in DHA Phase 9 Prism. Perfect dimensions (50x90), ideal for custom villa construction by Designer Homes.",
                features = "Corner + Park Facing, 60ft Wide Road, 100% Solid Soil, Direct Owner",
                builderTag = "Hot Plot",
                imageType = "plot"
            ),
            PropertyListing(
                id = "DH-DHA-305",
                title = "5 Marla Residential Plot in Hot Block",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.DHA,
                sectorOrBlock = "Phase 7, Block T",
                size = "5 Marla",
                priceDisplay = "PKR 1.35 Crore",
                priceNumeric = 13500000L,
                description = "Ready for immediate construction in Phase 7. Surrounding houses already populated. Best price in current market.",
                features = "Near Sector Mosque, Level Ground, Electricity & Gas Available, Fast Transfer",
                builderTag = "Direct Sale",
                imageType = "plot"
            ),

            // BAHRIA TOWN LAHORE
            PropertyListing(
                id = "DH-BAH-401",
                title = "10 Marla Custom Built Spanish House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.BAHRIA,
                sectorOrBlock = "Sector C (Near Grand Mosque)",
                size = "10 Marla",
                priceDisplay = "PKR 4.25 Crore",
                priceNumeric = 42500000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 3750,
                description = "Walking distance from Bahria Grand Jamia Mosque. Premium construction with solid foundations, imported Turkish fittings and designer gypsum ceiling.",
                features = "5 Beds with Attached Baths, Walking Distance to Mosque, 2 Kitchens, Servant Quarter",
                builderTag = "Designer Homes Exclusive",
                imageType = "house_spanish"
            ),
            PropertyListing(
                id = "DH-BAH-402",
                title = "5 Marla Luxury Boutique House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.BAHRIA,
                sectorOrBlock = "Sector F (Near Eiffel Tower)",
                size = "5 Marla",
                priceDisplay = "PKR 2.10 Crore",
                priceNumeric = 21000000L,
                bedrooms = 3,
                bathrooms = 4,
                coveredAreaSqFt = 2100,
                description = "Modern stylish house with luxury finishing. Close to Eiffel Tower park and commercial avenues.",
                features = "3 Bedrooms, American Style Open Kitchen, Balcony, Prime Bahria Location",
                builderTag = "Value For Money",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-BAH-403",
                title = "5 Marla Commercial Plot on Main Boulevard",
                category = PropertyCategory.COMMERCIAL,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.BAHRIA,
                sectorOrBlock = "Sector CC Commercial",
                size = "5 Marla",
                priceDisplay = "PKR 4.50 Crore",
                priceNumeric = 45000000L,
                description = "Approved for Basement + Ground + 4 Floors commercial plaza construction. Guaranteed high rental yield.",
                features = "Main Boulevard 120ft, High Footfall, Approved for G+4 Floors, High Rental Return",
                builderTag = "Commercial Asset",
                imageType = "commercial"
            ),

            // JOHAR TOWN LAHORE
            PropertyListing(
                id = "DH-JOH-501",
                title = "1 Kanal Solid Double Story Bungalow",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.JOHAR,
                sectorOrBlock = "Block G-3 (5 mins from Emporium Mall)",
                size = "1 Kanal",
                priceDisplay = "PKR 7.50 Crore",
                priceNumeric = 75000000L,
                bedrooms = 6,
                bathrooms = 7,
                coveredAreaSqFt = 5200,
                description = "Prime location in heart of Johar Town. Robust construction with spacious lawns, solid Sheesham wood doors, separate entrance for both floors.",
                features = "Dual Family Independent Unit, 6 Bedrooms, Near Emporium & Expo Center, Huge Car Porch",
                builderTag = "Prime Location",
                imageType = "house_spanish"
            ),
            PropertyListing(
                id = "DH-JOH-502",
                title = "10 Marla Commercial Plaza / Plot",
                category = PropertyCategory.COMMERCIAL,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.JOHAR,
                sectorOrBlock = "Main Boulevard Johar Town (Doctors Hospital Belt)",
                size = "10 Marla",
                priceDisplay = "PKR 8.20 Crore",
                priceNumeric = 82000000L,
                description = "Golden commercial location on busy boulevard. Ideal for corporate office, clinic, bank or branded showroom.",
                features = "Commercial Frontage, Massive Traffic Flow, Ample Parking, Immediate Possession",
                builderTag = "High Yield",
                imageType = "commercial"
            ),

            // MODEL TOWN LAHORE
            PropertyListing(
                id = "DH-MOD-601",
                title = "1 Kanal Classic Architectural Bungalow",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.MODEL,
                sectorOrBlock = "Block C (Central Park Belt)",
                size = "1 Kanal",
                priceDisplay = "PKR 14.0 Crore",
                priceNumeric = 140000000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 5400,
                description = "Prestigious address in Model Town Society. Lush green lawn, traditional brick facade, high ceilings, serene upscale environment.",
                features = "Model Town Society Security, Mature Green Trees, Grand Hallways, Rare Availability",
                builderTag = "Elite Heritage",
                imageType = "house_spanish"
            ),

            // WAPDA TOWN LAHORE
            PropertyListing(
                id = "DH-WAP-701",
                title = "10 Marla Brand New Double Story House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.WAPDA,
                sectorOrBlock = "Block D-2 (Opposite Main Market)",
                size = "10 Marla",
                priceDisplay = "PKR 3.90 Crore",
                priceNumeric = 39000000L,
                bedrooms = 5,
                bathrooms = 6,
                coveredAreaSqFt = 3600,
                description = "Modern elevation, spacious rooms with attached baths, designer kitchen with pantry, close to Ring Road and Shaukat Khanum.",
                features = "5 Master Bedrooms, 2 Modern Kitchens, Servant Quarter, 2 Car Garage, Wapda Town Security",
                builderTag = "Ready to Move",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-WAP-702",
                title = "5 Marla Prime Residential Plot",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.WAPDA,
                sectorOrBlock = "Wapda Town Extension, Block F",
                size = "5 Marla",
                priceDisplay = "PKR 1.15 Crore",
                priceNumeric = 11500000L,
                description = "Solid soil plot ready for house construction. Near school and commercial center. Designer Homes can build turnkey within 7 months.",
                features = "Level Land, Gas & Electricity Available, Fast Registry / Transfer",
                builderTag = "Best Value",
                imageType = "plot"
            ),

            // ETIHAD TOWN LAHORE (RAIWIND ROAD)
            PropertyListing(
                id = "DH-ETH-801",
                title = "5 Marla Ultra Modern Turnkey House",
                category = PropertyCategory.HOUSE,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.ETIHAD,
                sectorOrBlock = "Phase 1, Block A (Raiwind Road)",
                size = "5 Marla",
                priceDisplay = "PKR 2.40 Crore",
                priceNumeric = 24000000L,
                bedrooms = 3,
                bathrooms = 4,
                coveredAreaSqFt = 2200,
                description = "Located right on Main Raiwind Road adjacent to Ring Road interchange. Gated community with underground electrification and modern clubhouse.",
                features = "Gated Community, 3 En-Suite Bedrooms, Designer Powder Room, 1 Min to Ring Road",
                builderTag = "Designer Homes Build",
                imageType = "house_modern"
            ),
            PropertyListing(
                id = "DH-ETH-802",
                title = "10 Marla Residential Plot in Premium Block",
                category = PropertyCategory.PLOT,
                purpose = ListingPurpose.FOR_SALE,
                society = LahoreSociety.ETIHAD,
                sectorOrBlock = "Phase 2, Block C",
                size = "10 Marla",
                priceDisplay = "PKR 1.80 Crore",
                priceNumeric = 18000000L,
                description = "High growth investment and future luxury living in Etihad Town Phase 2. LDA approved society.",
                features = "LDA Approved, Underground Utilities, Parks & Grand Mosque, Installment / Cash",
                builderTag = "High Potential",
                imageType = "plot"
            ),

            // TURNKEY CONSTRUCTION & ARCHITECTURAL PACKAGES (SERVICES)
            PropertyListing(
                id = "DH-SRV-901",
                title = "5 Marla Turnkey Complete House Construction Package",
                category = PropertyCategory.CONSTRUCTION,
                purpose = ListingPurpose.SERVICE,
                society = LahoreSociety.ALL,
                sectorOrBlock = "Any Lahore Society (Valencia, Lake City, DHA, Bahria, etc.)",
                size = "5 Marla (~2,250 Sq Ft)",
                priceDisplay = "From PKR 1.22 Crore (Turnkey A+)",
                priceNumeric = 12200000L,
                description = "Complete peace of mind turnkey construction by Designer Homes Pakistan. Includes architectural 2D/3D maps, DHA/LDA approval, Awal bricks, Grade 60 steel, Spanish tiles, solid wood doors, modular kitchen and 1-year free maintenance warranty.",
                features = "Full Labor + Material + Architecture, 8 Months Completion, 1-Year Warranty, CCTV Live Site Monitoring",
                builderTag = "Signature Service",
                imageType = "construction"
            ),
            PropertyListing(
                id = "DH-SRV-902",
                title = "10 Marla Luxury House Turnkey Construction",
                category = PropertyCategory.CONSTRUCTION,
                purpose = ListingPurpose.SERVICE,
                society = LahoreSociety.ALL,
                sectorOrBlock = "Lahore All Societies",
                size = "10 Marla (~3,800 Sq Ft)",
                priceDisplay = "From PKR 2.05 Crore (Turnkey A+)",
                priceNumeric = 20500000L,
                description = "Premium grade construction with architectural double height lobby, Spanish tiles, Grohe sanitary ware, solid ash wood doors, modular acrylic kitchens, and designer false ceiling.",
                features = "Turnkey Execution, Itemized Transparent BOQ, Experienced Civil Engineers, Weekly Progress Reports",
                builderTag = "Top Recommended",
                imageType = "construction"
            ),
            PropertyListing(
                id = "DH-SRV-903",
                title = "Architectural 2D Maps, 3D Elevation & DHA/LDA Approvals",
                category = PropertyCategory.DESIGN,
                purpose = ListingPurpose.SERVICE,
                society = LahoreSociety.ALL,
                sectorOrBlock = "All Lahore Societies",
                size = "Any Plot Size",
                priceDisplay = "PKR 60 / Sq Ft (Complete Package)",
                priceNumeric = 135000L,
                description = "Get your house designed by senior Pakistani architects. Full package includes 2D floor plans, photorealistic 3D elevations, structural engineering drawings, MEP schematics, and official LDA/DHA/Bahria approval files.",
                features = "2D Floor Plans, 4K 3D Exterior Elevation, Structural Drawings, LDA/DHA Submission Blueprint, 3 Site Visits",
                builderTag = "Architectural Studio",
                imageType = "design"
            )
        )
    }
}
