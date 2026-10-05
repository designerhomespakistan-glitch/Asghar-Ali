package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.components.QuickInquiryDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.EstimatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyActivityScreen
import com.example.ui.screens.PostAdScreen
import com.example.ui.screens.PropertyDetailScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.DesignerHomesViewModel
import com.example.util.WhatsAppLauncher

class MainActivity : ComponentActivity() {

    private val viewModel: DesignerHomesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            com.example.ui.theme.MyApplicationTheme {
                val context = LocalContext.current
                val selectedTab by viewModel.selectedTab.collectAsState()
                val selectedDetail by viewModel.selectedPropertyDetail.collectAsState()
                val showInquiryDialog by viewModel.showQuickInquiryDialog.collectAsState()
                val inquiryPreset by viewModel.quickInquiryPreset.collectAsState()
                val favorites by viewModel.favorites.collectAsState()

                // Back handling: If on a sub-tab (not Home), return to Home
                if (selectedDetail == null && selectedTab != 0) {
                    BackHandler {
                        viewModel.selectTab(0)
                    }
                }

                if (selectedDetail != null) {
                    PropertyDetailScreen(
                        listing = selectedDetail!!,
                        isFavorite = favorites.contains(selectedDetail!!.id),
                        onFavoriteToggle = { viewModel.toggleFavorite(selectedDetail!!.id) },
                        onBack = { viewModel.closePropertyDetail() },
                        onOpenInquiry = { viewModel.openQuickInquiry(selectedDetail) }
                    )
                } else {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Column {
                                        Text(
                                            text = "DESIGNER HOMES",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = "Pakistan • 03364251212",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = GoldAccent
                                        )
                                    }
                                },
                                actions = {
                                    // Quick WhatsApp button in top bar
                                    IconButton(
                                        onClick = {
                                            val msg = "Assalam o Alaikum Designer Homes Pakistan! I am connecting from the app for project updates. (03364251212)"
                                            WhatsAppLauncher.openWhatsApp(context, msg)
                                        },
                                        modifier = Modifier.testTag("topbar_whatsapp_button")
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = WhatsAppGreen,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Chat,
                                                    contentDescription = "WhatsApp 03364251212",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Quick Call button in top bar
                                    IconButton(
                                        onClick = {
                                            WhatsAppLauncher.dialBuilder(context)
                                        },
                                        modifier = Modifier.testTag("topbar_call_button")
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = NavySecondary,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Call,
                                                    contentDescription = "Call 03364251212",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = NavyDark,
                                    titleContentColor = Color.White
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = NavyDark,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { viewModel.selectTab(0) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Properties") },
                                    label = { Text("Properties", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NavyDark,
                                        selectedTextColor = GoldAccent,
                                        indicatorColor = GoldAccent,
                                        unselectedIconColor = Color(0xFF94A3B8),
                                        unselectedTextColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_properties")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { viewModel.selectTab(1) },
                                    icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Post Ad") },
                                    label = { Text("Post Ad", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NavyDark,
                                        selectedTextColor = GoldAccent,
                                        indicatorColor = GoldAccent,
                                        unselectedIconColor = Color(0xFF94A3B8),
                                        unselectedTextColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_post_ad")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 2,
                                    onClick = { viewModel.selectTab(2) },
                                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Estimator") },
                                    label = { Text("Estimator", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NavyDark,
                                        selectedTextColor = GoldAccent,
                                        indicatorColor = GoldAccent,
                                        unselectedIconColor = Color(0xFF94A3B8),
                                        unselectedTextColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_estimator")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 3,
                                    onClick = { viewModel.selectTab(3) },
                                    icon = { Icon(Icons.Default.History, contentDescription = "Activity") },
                                    label = { Text("My Leads", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NavyDark,
                                        selectedTextColor = GoldAccent,
                                        indicatorColor = GoldAccent,
                                        unselectedIconColor = Color(0xFF94A3B8),
                                        unselectedTextColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_activity")
                                )

                                NavigationBarItem(
                                    selected = selectedTab == 4,
                                    onClick = { viewModel.selectTab(4) },
                                    icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                                    label = { Text("About", fontSize = 11.sp) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NavyDark,
                                        selectedTextColor = GoldAccent,
                                        indicatorColor = GoldAccent,
                                        unselectedIconColor = Color(0xFF94A3B8),
                                        unselectedTextColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier.testTag("nav_tab_about")
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            label = "tab_content_animation"
                        ) { tab ->
                            when (tab) {
                                0 -> HomeScreen(viewModel = viewModel)
                                1 -> PostAdScreen(viewModel = viewModel)
                                2 -> EstimatorScreen(viewModel = viewModel)
                                3 -> MyActivityScreen(viewModel = viewModel)
                                4 -> AboutScreen(onOpenInquiry = { viewModel.openQuickInquiry(null) })
                            }
                        }
                    }
                }

                // Quick Inquiry Modal Dialog
                if (showInquiryDialog) {
                    QuickInquiryDialog(
                        presetListing = inquiryPreset,
                        onDismiss = { viewModel.closeQuickInquiry() },
                        onSubmit = { name, phone, society, details ->
                            viewModel.submitQuickInquiry(context, name, phone, society, details)
                        }
                    )
                }
            }
        }
    }
}
