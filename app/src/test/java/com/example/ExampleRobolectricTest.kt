package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.LahoreSociety
import com.example.data.model.LeadSubmission
import com.example.data.model.ListingPurpose
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyListing
import com.example.util.CostCalculator
import com.example.util.WhatsAppLauncher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Designer Homes", appName)
    }

    @Test
    fun `cost calculator computes correct turnkey cost for 5 marla`() {
        val estimate = CostCalculator.calculate("5 Marla", null, CostCalculator.ConstructionPackage.A_PLUS_TURNKEY)
        assertEquals(2250, estimate.coveredAreaSqFt)
        assertEquals(5400, estimate.ratePerSqFt)
        assertEquals(2250L * 5400L, estimate.totalCostPKR)
    }

    @Test
    fun `whatsapp launcher formats lead inquiry with phone number`() {
        val lead = LeadSubmission(
            leadType = "POST_AD",
            clientName = "Malik Tariq",
            clientPhone = "03001234567",
            society = "Valencia Town",
            propertyCategory = "House",
            size = "10 Marla",
            budgetOrDemand = "3.85 Crore",
            details = "Facing park Spanish house"
        )
        val formatted = WhatsAppLauncher.formatLeadInquiryMessage(lead)
        assertTrue(formatted.contains("03364251212"))
        assertTrue(formatted.contains("Valencia Town"))
        assertTrue(formatted.contains("10 Marla"))
    }
}
