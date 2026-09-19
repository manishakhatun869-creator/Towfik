package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandSky400
import com.example.ui.theme.TowfikPrimaryBlue
import com.example.ui.theme.premiumBorder
import com.example.ui.theme.premiumShadow
import com.example.ui.theme.premiumSurface
import com.example.ui.theme.premiumTextTertiary
import com.example.ui.theme.rememberPressInteraction
import com.example.viewmodel.MainViewModel

data class NavTab(val title: String, val icon: ImageVector, val tag: String)

@Composable
fun TowfikApp(
  viewModel: MainViewModel = viewModel()
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val dark = isSystemInDarkTheme()

  val selectedItemForPdf by viewModel.selectedItemForPdf.collectAsState()
  val showAddEditDialog by viewModel.showAddEditDialog.collectAsState()
  val editingItem by viewModel.editingItem.collectAsState()

  val isAdmin by viewModel.isAdminLoggedIn.collectAsState()

  val navTabs = listOf(
    NavTab("Home", Icons.Default.Home, "nav_home"),
    NavTab("Search", Icons.Default.Search, "nav_search"),
    NavTab("Notes", Icons.Default.MenuBook, "nav_notes"),
    NavTab("PYQ Papers", Icons.Default.Quiz, "nav_pyq"),
    NavTab("PDFs", Icons.Default.PictureAsPdf, "nav_pdfs"),
    NavTab("Admin Panel", Icons.Default.AdminPanelSettings, "nav_admin"),
  )

  val accent = if (dark) BrandSky400 else TowfikPrimaryBlue

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = if (dark) Color(0xFF0A1120) else Color(0xFFF4F6FB),
    floatingActionButton = {
      // Show + FAB on tabs ONLY when Admin is logged in, and never on Search tab (index 1)
      if (isAdmin && selectedTab != 1) {
        val fabInteraction = rememberPressInteraction()
        Box(
          modifier = Modifier
            .size(60.dp)
            .premiumShadow(elevation = 12.dp, shape = CircleShape, alpha = 0.35f)
            .clip(CircleShape)
            .background(BrandGradient)
            .border(1.5.dp, Color.White.copy(alpha = 0.35f), CircleShape)
            .clickable(
              interactionSource = fabInteraction,
              indication = null,
              onClick = { viewModel.openAddEditDialog(null) },
            )
            .testTag("admin_global_fab_add_btn"),
          contentAlignment = Alignment.Center,
        ) {
          // Gloss highlight
          Box(
            modifier = Modifier
              .matchParentSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.White.copy(alpha = 0.22f), Color.Transparent),
                ),
              ),
          )
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Question / Suggestion",
            tint = Color.White,
            modifier = Modifier.size(28.dp),
          )
        }
      }
    },
    bottomBar = {
      NavigationBar(
        containerColor = premiumSurface(),
        tonalElevation = 0.dp,
        modifier = Modifier
          .testTag("main_bottom_nav_bar")
          .border(
            width = 1.dp,
            color = premiumBorder(),
            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
          )
          .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
      ) {
        navTabs.forEachIndexed { index, tab ->
          val isSelected = selectedTab == index
          val iconSize by animateDpAsState(
            targetValue = if (isSelected) 25.dp else 22.dp,
            animationSpec = tween(220),
            label = "navIcon",
          )
          NavigationBarItem(
            selected = isSelected,
            onClick = { selectedTab = index },
            icon = {
              // Premium selected icon: gradient tile; unselected: plain icon
              if (isSelected) {
                Box(
                  modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(BrandGradient),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.title,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize),
                  )
                }
              } else {
                Icon(
                  imageVector = tab.icon,
                  contentDescription = tab.title,
                  tint = premiumTextTertiary(),
                  modifier = Modifier.size(iconSize),
                )
              }
            },
            label = {
              Text(
                text = tab.title,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                maxLines = 1,
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color.White,
              selectedTextColor = accent,
              indicatorColor = Color.Transparent,
              unselectedIconColor = premiumTextTertiary(),
              unselectedTextColor = premiumTextTertiary(),
            ),
            modifier = Modifier.testTag(tab.tag),
          )
        }
      }
    },
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      AnimatedContent(
        targetState = selectedTab,
        transitionSpec = {
          (fadeIn(animationSpec = tween(260)) + scaleIn(initialScale = 0.985f, animationSpec = tween(260))) togetherWith
            (fadeOut(animationSpec = tween(180)) + scaleOut(targetScale = 0.985f, animationSpec = tween(180)))
        },
        label = "ScreenTransition",
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

      // PDF Viewer Modal
      selectedItemForPdf?.let { item ->
        PdfViewerModal(
          item = item,
          onDismiss = { viewModel.closePdfPreview() },
        )
      }

      // Admin Add/Edit Dialog
      if (showAddEditDialog) {
        val currentSubjects by viewModel.subjects.collectAsState()
        AddEditMaterialDialog(
          initialItem = editingItem,
          availableSubjects = currentSubjects,
          onSave = { savedItem ->
            viewModel.saveStudyItem(savedItem)
            viewModel.closeAddEditDialog()
          },
          onDismiss = { viewModel.closeAddEditDialog() },
        )
      }
    }
  }
}
