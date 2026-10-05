package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.model.ConstructionEstimate
import com.example.data.model.LeadSubmission
import com.example.data.model.PropertyListing
import java.net.URLEncoder

object WhatsAppLauncher {

    const val OFFICIAL_PHONE_DISPLAY = "03364251212"
    const val OFFICIAL_PHONE_INTL = "923364251212"
    const val COMPANY_NAME = "Designer Homes Pakistan"

    /**
     * Opens WhatsApp chat with prefilled message to 03364251212
     */
    fun openWhatsApp(context: Context, message: String, phoneNumber: String = OFFICIAL_PHONE_INTL) {
        try {
            val encodedMessage = URLEncoder.encode(message, "UTF-8")
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=$encodedMessage"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                // Secondary fallback using direct wa.me
                val encodedMessage = URLEncoder.encode(message, "UTF-8")
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber?text=$encodedMessage")).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Could not open WhatsApp: ${e2.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Opens phone dialer prefilled with 03364251212
     */
    fun dialBuilder(context: Context, phoneNumber: String = OFFICIAL_PHONE_DISPLAY) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open dialer: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Generates a lead message for a posted property ad
     */
    fun formatPostedAdMessage(listing: PropertyListing): String {
        return """
            🏛️ *NEW PROPERTY AD / LISTING LEAD*
            *Designer Homes Pakistan*
            
            📍 *Location / Society:* ${listing.society.displayName} (${listing.sectorOrBlock})
            🏷️ *Property Type:* ${listing.category.displayName}
            🎯 *Purpose:* ${listing.purpose.displayName}
            📏 *Size:* ${listing.size}
            💰 *Demand / Price:* ${listing.priceDisplay}
            
            📝 *Title:* ${listing.title}
            📋 *Details:* ${listing.description}
            ⭐ *Key Features:* ${listing.features}
            
            👤 *Client Contact:* ${listing.contactPhone}
            📅 *Ref ID:* ${listing.id}
            
            _Sent via Designer Homes Pakistan Mobile App for immediate WhatsApp response (03364251212)_
        """.trimIndent()
    }

    /**
     * Formats lead message for general inquiries or requirements
     */
    fun formatLeadInquiryMessage(lead: LeadSubmission): String {
        val leadTypeDisplay = when (lead.leadType) {
            "POST_AD" -> "New Property Ad / Sale Lead"
            "BUY_PLOT" -> "Plot Buying Requirement"
            "BUY_HOUSE" -> "House Purchase Requirement"
            "CONSTRUCTION_INQUIRY" -> "Construction & Builder Inquiry"
            "DESIGN_CONSULTATION" -> "Architectural 2D/3D Design Consultation"
            "SITE_VISIT" -> "Site Visit / Project Meeting Request"
            else -> "Real Estate / Construction Lead"
        }

        return """
            🏗️ *DESIGNER HOMES PAKISTAN - NEW LEAD*
            *Type:* $leadTypeDisplay
            
            👤 *Client Name:* ${lead.clientName.ifBlank { "Client" }}
            📞 *Client Phone:* ${lead.clientPhone.ifBlank { "Provided on Chat" }}
            📍 *Target Society:* ${lead.society}
            🏷️ *Category:* ${lead.propertyCategory}
            📏 *Size / Area:* ${lead.size}
            💵 *Budget / Demand:* ${lead.budgetOrDemand}
            
            📝 *Client Requirements / Notes:*
            ${lead.details}
            
            📲 *Direct Builder Follow-up requested for 03364251212*
        """.trimIndent()
    }

    /**
     * Formats property inquiry message from property details
     */
    fun formatPropertyInquiry(listing: PropertyListing): String {
        return """
            Assalam o Alaikum Designer Homes Pakistan!
            
            I am interested in this listing:
            🏠 *${listing.title}*
            📍 *Society:* ${listing.society.displayName} - ${listing.sectorOrBlock}
            📏 *Size:* ${listing.size}
            💰 *Demand:* ${listing.priceDisplay}
            🆔 *ID:* ${listing.id}
            
            Please share more details, original video walkthrough, and availability for a site visit.
            
            Thank you!
        """.trimIndent()
    }

    /**
     * Formats construction quotation request
     */
    fun formatConstructionEstimate(estimate: ConstructionEstimate, clientName: String, clientPhone: String, society: String): String {
        val totalCostMillions = String.format("%.2f", estimate.totalCostPKR / 10000000.0)
        val grayCostMillions = String.format("%.2f", estimate.grayStructureCost / 10000000.0)
        val finishCostMillions = String.format("%.2f", estimate.finishingCost / 10000000.0)

        return """
            🏗️ *CONSTRUCTION ESTIMATE & QUOTATION REQUEST*
            *Designer Homes Pakistan - Real Estate & Builders*
            
            👤 *Client Name:* ${clientName.ifBlank { "Respected Client" }}
            📞 *Phone:* ${clientPhone.ifBlank { "Attached on WhatsApp" }}
            📍 *Location / Society:* $society
            
            📐 *Plot Size:* ${estimate.plotSize}
            🏠 *Covered Area:* ~${estimate.coveredAreaSqFt} Sq. Ft.
            📦 *Selected Package:* ${estimate.packageType}
            📊 *Estimated Rate:* PKR ${estimate.ratePerSqFt} / Sq. Ft.
            
            💵 *Total Estimated Budget:* ~PKR $totalCostMillions Crore
               - Gray Structure Portion: ~PKR $grayCostMillions Crore
               - Finishing / Turnkey: ~PKR $finishCostMillions Crore
            ⏳ *Estimated Completion Time:* ${estimate.estimatedDurationMonths} Months
            
            ✨ *Specifications & Scope:*
            ${estimate.materialHighlights.joinToString("\n") { "• $it" }}
            
            Please schedule a technical meeting and send detailed itemized BOQ to this number (03364251212).
        """.trimIndent()
    }

    /**
     * Share text via Android system picker
     */
    fun shareText(context: Context, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }
}
