package com.example.util

import com.example.data.model.ConstructionEstimate

object CostCalculator {

    val PLOT_SIZES = listOf(
        "3 Marla" to 1350,
        "5 Marla" to 2250,
        "7 Marla" to 2900,
        "10 Marla" to 3800,
        "1 Kanal" to 5800,
        "2 Kanal" to 10500,
        "Custom Area" to 2500
    )

    enum class ConstructionPackage(
        val title: String,
        val shortDesc: String,
        val ratePerSqFt: Int,
        val estimatedMonths: Int,
        val highlights: List<String>
    ) {
        GRAY_STRUCTURE(
            title = "Gray Structure with A-Grade Material",
            shortDesc = "Foundation, RCC roof, bricks, Grade 60 steel & piping",
            ratePerSqFt = 2950,
            estimatedMonths = 5,
            highlights = listOf(
                "Awal Quality Bricks with uniform water curing",
                "Grade 60 Deformed Steel Bars (Amreli / Mughal Supreme)",
                "DG / Maple Leaf / Bestway OPC & SRC Cement",
                "Chenab Sand for plaster & Ravi Sand for masonry",
                "Margalla Clean Crushed Stone for RCC slabs & columns",
                "PPRC water supply pipes & PVC drainage network",
                "Underground reinforced water tank & Overhead concrete tank",
                "Complete anti-termite chemical proofing injection"
            )
        ),
        A_PLUS_TURNKEY(
            title = "A+ Signature Luxury Turnkey (Most Popular)",
            shortDesc = "Complete key-in-hand modern designer house finishing",
            ratePerSqFt = 5400,
            estimatedMonths = 9,
            highlights = listOf(
                "Complete Gray Structure + Full Luxury Turnkey Finishing",
                "Spanish & Imported Porcelain Floor & Wall Tiles (60x120cm)",
                "Solid Ash Wood Doors with antique designer Italian handles",
                "Designer Turkish / European sanitary ware & Grohe fittings",
                "Modern Modular Kitchen with Corian / Granite tops & UV/Acrylic cabinets",
                "Full Designer Gypsum False Ceiling with magnetic track LED lighting",
                "Imported Tempered 12mm Glass Stair & Balcony Railings",
                "Premium Weather-shield exterior rockwall & thermal insulation"
            )
        ),
        EXECUTIVE_VILLA(
            title = "Executive Designer Luxury Villa",
            shortDesc = "Ultra-premium imported marble, smart home & bespoke architecture",
            ratePerSqFt = 6700,
            estimatedMonths = 12,
            highlights = listOf(
                "Imported Italian Botticino / Verona / Spanish Marble flooring",
                "Smart Home Automation (lighting, HVAC & security integrations)",
                "Architectural Double-Height lobby with chandelier & feature wall",
                "Customized Walk-in Closets & Wardrobes with integrated LEDs",
                "Double Glazed Powder-Coated Aluminum / UPVC soundproof windows",
                "Solar System Conduit Infrastructure & Central UPS wiring",
                "Designer Powder Room with Onyx translucent backlit basin",
                "Private Rooftop BBQ Pergola & landscaped green terrace"
            )
        ),
        ARCHITECTURAL_DESIGN_ONLY(
            title = "Architectural Map & 3D Design Package",
            shortDesc = "Complete floor plans, 3D elevation & LDA/DHA approval blueprints",
            ratePerSqFt = 60,
            estimatedMonths = 1,
            highlights = listOf(
                "Custom Architectural 2D Floor Plans (Vastu / Sun orientation)",
                "Ultra-realistic 4K 3D Exterior Elevation Renders & Night Lighting",
                "3D Interior Layout concepts for Master Bed, Drawing & TV Lounge",
                "Structural Engineering blueprints & RCC schedule calculation",
                "Plumbing, Water Supply & Electrical Conduit Layout Schematics",
                "Official LDA / DHA / Bahria / Lake City Building Bye-Laws Approval Submission File",
                "Detailed Bill of Quantities (BOQ) with material volume estimation",
                "3 Site Supervision Visits by Senior Architects in Lahore"
            )
        )
    }

    fun calculate(
        plotSize: String,
        customAreaSqFt: Int?,
        packageType: ConstructionPackage
    ): ConstructionEstimate {
        val coveredArea = if (plotSize == "Custom Area" && customAreaSqFt != null && customAreaSqFt > 0) {
            customAreaSqFt
        } else {
            PLOT_SIZES.find { it.first == plotSize }?.second ?: 2250
        }

        val rate = packageType.ratePerSqFt
        val totalCost = coveredArea.toLong() * rate

        // Breakdowns
        val grayStructurePart: Long
        val finishingPart: Long
        val designCost: Long

        when (packageType) {
            ConstructionPackage.GRAY_STRUCTURE -> {
                grayStructurePart = totalCost
                finishingPart = 0
                designCost = (coveredArea * 45).toLong()
            }
            ConstructionPackage.A_PLUS_TURNKEY -> {
                grayStructurePart = (coveredArea * 2950).toLong()
                finishingPart = totalCost - grayStructurePart
                designCost = (coveredArea * 50).toLong()
            }
            ConstructionPackage.EXECUTIVE_VILLA -> {
                grayStructurePart = (coveredArea * 3200).toLong()
                finishingPart = totalCost - grayStructurePart
                designCost = (coveredArea * 65).toLong()
            }
            ConstructionPackage.ARCHITECTURAL_DESIGN_ONLY -> {
                grayStructurePart = 0
                finishingPart = 0
                designCost = totalCost
            }
        }

        return ConstructionEstimate(
            plotSize = plotSize,
            coveredAreaSqFt = coveredArea,
            packageType = packageType.title,
            ratePerSqFt = rate,
            totalCostPKR = totalCost,
            grayStructureCost = grayStructurePart,
            finishingCost = finishingPart,
            architecturalDesignCost = designCost,
            estimatedDurationMonths = packageType.estimatedMonths,
            materialHighlights = packageType.highlights
        )
    }
}
