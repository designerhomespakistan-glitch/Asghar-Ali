package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppLauncher

@Composable
fun AboutScreen(
    onOpenInquiry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Company Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = NavyDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = GoldAccent,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = NavyDark,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "DESIGNER HOMES PAKISTAN",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Premier Real Estate & Builders • Lahore",
                    color = GoldAccent,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Direct Line & WhatsApp for Quick Project Updates:",
                    color = Color(0xFFCBD5E1),
                    fontSize = 11.sp
                )
                Text(
                    text = "03364251212",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val msg = "Assalam o Alaikum Designer Homes Pakistan! I am connecting via your Android app. Please share your current available projects and construction portfolio in Lahore. (03364251212)"
                            WhatsAppLauncher.openWhatsApp(context, msg)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WhatsAppGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("about_whatsapp_btn")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { WhatsAppLauncher.dialBuilder(context) },
                        border = BorderStroke(1.dp, GoldAccent),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("about_call_btn")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Direct", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Key Lahore Societies Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Prime Lahore Societies We Cover",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                val societies = listOf(
                    "Valencia Town" to "Block A to P, Commercial, Spanish Villas & Plots",
                    "Lake City Lahore" to "Sector M1 to M8, Golf Estates & Ready Villas",
                    "DHA Lahore" to "Phases 1, 2, 3, 4, 5, 6, 7, 8, 9 Prism & Rahbar",
                    "Bahria Town Lahore" to "Sector A, B, C, D, E, F & Commercial Plazas",
                    "Johar Town Lahore" to "Near Emporium, Doctors Hospital, Commercials",
                    "Model Town Lahore" to "Heritage Bungalows, Plots & Renovation",
                    "Wapda Town Lahore" to "Phase 1, Phase 2, Wapda Extension",
                    "Etihad Town" to "Phase 1 & Phase 2 on Main Raiwind Road",
                    "Raiwind Road Belt" to "Al-Kabir Town, Lake City, Etihad, Superior Belt"
                )

                societies.forEach { (name, desc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
                            Text(text = desc, fontSize = 11.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }

        // Scope of Services
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Builder & Real Estate Services",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                ServiceRow(
                    icon = Icons.Default.Home,
                    title = "Turnkey Luxury Construction",
                    desc = "Complete A+ key-in-hand residential & commercial construction with 1-year maintenance guarantee."
                )
                ServiceRow(
                    icon = Icons.Default.Architecture,
                    title = "Architectural 2D/3D Design",
                    desc = "Vastu/climate-compliant 2D plans, 4K realistic 3D exterior/interior elevation renders."
                )
                ServiceRow(
                    icon = Icons.Default.Apartment,
                    title = "DHA & LDA Submission Blueprints",
                    desc = "Complete structural engineering calculations and legal building bye-laws approval files."
                )
                ServiceRow(
                    icon = Icons.Default.Security,
                    title = "Plot Buying & Selling Advisory",
                    desc = "Transparent direct owner deals for genuine residential and commercial plots across Lahore."
                )
            }
        }

        // CTA: Request Site Visit
        Button(
            onClick = onOpenInquiry,
            colors = ButtonDefaults.buttonColors(
                containerColor = NavyPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("request_site_visit_btn")
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Request Free Site Visit in Lahore", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ServiceRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF1F5F9),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NavyPrimary)
            Text(text = desc, fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 15.sp)
        }
    }
}
