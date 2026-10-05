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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Schedule
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
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.DesignerHomesViewModel
import com.example.util.CostCalculator
import com.example.util.CostCalculator.ConstructionPackage
import com.example.util.WhatsAppLauncher

@Composable
fun EstimatorScreen(
    viewModel: DesignerHomesViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedPlotSize by viewModel.selectedPlotSize.collectAsState()
    val customArea by viewModel.customAreaSqFt.collectAsState()
    val selectedPackage by viewModel.selectedPackage.collectAsState()
    val estimate by viewModel.currentEstimate.collectAsState()
    val society by viewModel.calcSociety.collectAsState()
    val clientName by viewModel.calcClientName.collectAsState()
    val clientPhone by viewModel.calcClientPhone.collectAsState()

    val societies = listOf(
        "Valencia Town",
        "Lake City",
        "DHA Phase 1-9",
        "Bahria Town",
        "Johar Town",
        "Model Town",
        "Wapda Town",
        "Etihad Town",
        "Raiwind Road"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Construction,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lahore Construction & Design Estimator",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Calculate covered area, Gray Structure, and Turnkey Luxury Finishing costs according to current Lahore market rates. Send directly to WhatsApp (03364251212) for detailed BOQ!",
                    color = Color(0xFFCBD5E1),
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }

        // 1. Select Plot Size
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Select Plot Size",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CostCalculator.PLOT_SIZES.forEach { (size, area) ->
                        val isSelected = selectedPlotSize == size
                        Surface(
                            onClick = { viewModel.setPlotSize(size) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) NavyPrimary else Color(0xFFF1F5F9),
                            border = BorderStroke(1.dp, if (isSelected) GoldAccent else Color(0xFFE2E8F0)),
                            modifier = Modifier.testTag("estimator_size_$size")
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = size,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF1E293B)
                                )
                                Text(
                                    text = "~$area sq ft",
                                    fontSize = 10.sp,
                                    color = if (isSelected) GoldAccent else Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }

                if (selectedPlotSize == "Custom Area") {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customArea,
                        onValueChange = { viewModel.setCustomArea(it) },
                        label = { Text("Enter Custom Covered Area (Sq. Ft.)") },
                        placeholder = { Text("e.g. 3500") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 2. Select Construction Package
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. Select Construction / Design Package",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                ConstructionPackage.values().forEach { pkg ->
                    val isSelected = selectedPackage == pkg
                    Surface(
                        onClick = { viewModel.setConstructionPackage(pkg) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFFFFFBEB) else Color(0xFFF8FAFC),
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) GoldDark else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("estimator_pkg_${pkg.name}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = pkg.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) NavyDark else Color(0xFF334155)
                                )
                                Text(
                                    text = pkg.shortDesc,
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B),
                                    lineHeight = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) GoldAccent else Color(0xFFE2E8F0)
                            ) {
                                Text(
                                    text = "PKR ${pkg.ratePerSqFt}/sqft",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NavyDark else Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Live Cost Calculation Breakdown Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.5.dp, GoldAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ESTIMATED TOTAL BUDGET",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        val totalCrores = estimate.totalCostPKR / 10000000.0
                        val totalLacs = estimate.totalCostPKR / 100000.0
                        val displayTotal = if (totalCrores >= 1.0) {
                            String.format("%.2f Crore", totalCrores)
                        } else {
                            String.format("%.1f Lacs", totalLacs)
                        }
                        Text(
                            text = "PKR $displayTotal",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "~${estimate.estimatedDurationMonths} Months",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Breakdown rows
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Covered Area", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text("${estimate.coveredAreaSqFt} Sq. Ft.", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Rate Applied", fontSize = 12.sp, color = Color(0xFF64748B))
                            Text("PKR ${estimate.ratePerSqFt} / Sq. Ft.", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        if (estimate.grayStructureCost > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gray Structure Approx", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text("PKR ${String.format("%.2f", estimate.grayStructureCost / 10000000.0)} Cr", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        if (estimate.finishingCost > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Luxury Finishing Approx", fontSize = 12.sp, color = Color(0xFF64748B))
                                Text("PKR ${String.format("%.2f", estimate.finishingCost / 10000000.0)} Cr", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Package Specifications Included:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                estimate.materialHighlights.forEach { highlight ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = highlight,
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // 4. Client Details & Target Society
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. Select Construction Society & Details",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    societies.forEach { soc ->
                        val isSelected = society == soc
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCalcSociety(soc) },
                            label = { Text(soc, fontSize = 12.sp) },
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
                    value = clientName,
                    onValueChange = { viewModel.setCalcClientName(it) },
                    label = { Text("Your Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { viewModel.setCalcClientPhone(it) },
                    label = { Text("Your Phone / WhatsApp") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.sendEstimateToWhatsApp(context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = WhatsAppGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("send_estimate_whatsapp_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Get Official Quotation on WhatsApp (03364251212)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
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
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Call Builder for Site Visit (03364251212)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
