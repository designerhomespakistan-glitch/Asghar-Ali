package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LahoreSociety
import com.example.data.model.ListingPurpose
import com.example.data.model.PropertyCategory
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.DesignerHomesViewModel
import com.example.util.WhatsAppLauncher

@Composable
fun PostAdScreen(
    viewModel: DesignerHomesViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val form by viewModel.postAdState.collectAsState()
    val scrollState = rememberScrollState()

    val quickSizes = listOf("3 Marla", "5 Marla", "7 Marla", "10 Marla", "1 Kanal", "2 Kanal")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner explaining the feature
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Publish,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Post Real Estate Ad / Lead",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Post your Plot, House, Commercial, Architectural Design, or Construction requirement. It publishes instantly in this app and routes directly to WhatsApp 03364251212 for quick deals & project updates!",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }

        // Section 1: Purpose & Category
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Purpose of Ad",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ListingPurpose.values().forEach { purp ->
                        val isSelected = form.purpose == purp
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updatePostAdForm { it.copy(purpose = purp) } },
                            label = { Text(purp.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "2. Property Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        PropertyCategory.HOUSE,
                        PropertyCategory.PLOT,
                        PropertyCategory.CONSTRUCTION,
                        PropertyCategory.DESIGN,
                        PropertyCategory.COMMERCIAL
                    ).forEach { cat ->
                        val isSelected = form.category == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updatePostAdForm { it.copy(category = cat) } },
                            label = { Text(cat.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldDark,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }
        }

        // Section 2: Lahore Society & Location
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. Society / Location in Lahore",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        LahoreSociety.VALENCIA,
                        LahoreSociety.LAKE_CITY,
                        LahoreSociety.DHA,
                        LahoreSociety.BAHRIA,
                        LahoreSociety.JOHAR,
                        LahoreSociety.MODEL,
                        LahoreSociety.WAPDA,
                        LahoreSociety.ETIHAD,
                        LahoreSociety.RAIWIND,
                        LahoreSociety.OTHER
                    ).forEach { soc ->
                        val isSelected = form.society == soc
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updatePostAdForm { it.copy(society = soc) } },
                            label = { Text(soc.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = form.sectorOrBlock,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(sectorOrBlock = it) } },
                    label = { Text("Sector / Block / Phase (e.g. Block K, Phase 6, Sector M3)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_sector_input")
                )
            }
        }

        // Section 3: Ad Details & Pricing
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. Property Specifications",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.title,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(title = it) } },
                    label = { Text("Ad Headline / Title *") },
                    placeholder = { Text("e.g. 10 Marla Brand New House in Valencia Town") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_title_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Select Size / Marla", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickSizes.forEach { sz ->
                        val isSelected = form.size == sz
                        Surface(
                            onClick = { viewModel.updatePostAdForm { f -> f.copy(size = sz) } },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) NavyPrimary else Color(0xFFF1F5F9),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = sz,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.demandPrice,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(demandPrice = it) } },
                    label = { Text("Demand / Budget Price *") },
                    placeholder = { Text("e.g. 2.85 Crore or 95 Lac or 45,000 Rent") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_price_input")
                )

                if (form.category == PropertyCategory.HOUSE) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = form.bedrooms,
                            onValueChange = { viewModel.updatePostAdForm { f -> f.copy(bedrooms = it) } },
                            label = { Text("Bedrooms") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = form.bathrooms,
                            onValueChange = { viewModel.updatePostAdForm { f -> f.copy(bathrooms = it) } },
                            label = { Text("Bathrooms") },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.features,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(features = it) } },
                    label = { Text("Key Features (comma separated)") },
                    placeholder = { Text("Park Facing, Corner, Solid Construction, Gas Available") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.description,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(description = it) } },
                    label = { Text("Complete Details / Demand Description") },
                    placeholder = { Text("Write complete description, road width, possession status, etc.") },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ad_description_input")
                )
            }
        }

        // Section 4: Contact Information
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "5. Your Contact Information",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.clientName,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(clientName = it) } },
                    label = { Text("Your Name") },
                    placeholder = { Text("e.g. Muhammad Ali") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = form.clientPhone,
                    onValueChange = { viewModel.updatePostAdForm { f -> f.copy(clientPhone = it) } },
                    label = { Text("Your Phone / WhatsApp Number") },
                    placeholder = { Text("e.g. 03001234567") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Submit Button Row
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.submitPostAd(context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("publish_ad_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Publish & Send to WhatsApp (03364251212)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            OutlinedButton(
                onClick = { WhatsAppLauncher.dialBuilder(context) },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, NavyPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("ad_direct_call_builder")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Call Builder Directly (03364251212)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
