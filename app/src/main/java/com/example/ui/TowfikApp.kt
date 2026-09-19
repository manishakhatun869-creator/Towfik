package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddEditMaterialDialog
import com.example.ui.components.PdfViewerModal
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesScreen
import com.example.ui.screens.PdfScreen
import com.example.ui.screens.PyqScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.TowfikLightBg
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.viewmodel.MainViewModel

data class NavTab(val title: String, val icon: ImageVector, val tag: String)

@Composable
fun TowfikApp(
    viewModel: MainViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val selectedItemForPdf by viewModel.selectedItemForPdf.collectAsState()
    val showAddEditDialog by viewModel.showAddEditDialog.collectAsState()
    val editingItem by viewModel.editingItem.collectAsState()

    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

    val navTabs = listOf(
        NavTab("Home", Icons.Default.Home, "nav_home"),
        NavTab("Search", Icons.Default.Search, "nav_search"),
        NavTab("Notes", Icons.Default.MenuBook, "nav_notes"),
        NavTab("PYQ", Icons.Default.Quiz, "nav_pyq"),
        NavTab("PDFs", Icons.Default.PictureAsPdf, "nav_pdfs"),
        NavTab("Admin", Icons.Default.AdminPanelSettings, "nav_admin")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = TowfikLightBg,
        floatingActionButton = {
            if (isAdmin && selectedTab != 1) {
                FloatingActionButton(
                    onClick = { viewModel.openAddEditDialog(null) },
                    containerColor = TowfikPrimaryBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .shadow(12.dp, CircleShape)
                        .testTag("admin_global_fab_add_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Question / Suggestion",
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
                    .testTag("main_bottom_nav_bar")
            ) {
                navTabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        alwaysShowLabel = false,
                        icon = {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) Color(0xFFEFF6FF) else Color.Transparent
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TowfikPrimaryBlue,
                            selectedTextColor = TowfikPrimaryBlue,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color(0xFF94A3B8),
                            unselectedTextColor = Color(0xFF94A3B8)
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(TowfikLightBg)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { targetTab ->
                when (targetTab) {
                    0 -> HomeScreen(viewModel = viewModel, onNavigateToTab = { selectedTab = it })
                    1 -> SearchScreen(viewModel = viewModel)
                    2 -> NotesScreen(viewModel = viewModel)
                    3 -> PyqScreen(viewModel = viewModel)
                    4 -> PdfScreen(viewModel = viewModel)
                    5 -> AdminScreen(viewModel = viewModel)
                }
            }

            selectedItemForPdf?.let { item ->
                PdfViewerModal(
                    item = item,
                    onDismiss = { viewModel.closePdfPreview() }
                )
            }

            if (showAddEditDialog) {
                val currentSubjects by viewModel.subjects.collectAsState()
                AddEditMaterialDialog(
                    initialItem = editingItem,
                    availableSubjects = currentSubjects,
                    onSave = { savedItem ->
                        viewModel.saveStudyItem(savedItem)
                        viewModel.closeAddEditDialog()
                    },
                    onDismiss = { viewModel.closeAddEditDialog() }
                )
            }
        }
    }
}
