package com.example.auth_v1.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.auth_v1.Constants.LetsConnectColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val mainState by mainViewModel.mainState.collectAsState()
    val selectedRoute by mainViewModel.selectedRoute.collectAsState()
    val user by mainViewModel.userFlow.collectAsState()
    
    LaunchedEffect(mainState) {
        if (mainState is MainState.LoggedOut) {
            onLogout()
        }
    }
    
    when (mainState) {
        is MainState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = LetsConnectColors.Primary2)
            }
        }
        
        is MainState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = (mainState as MainState.Error).message,
                        color = LetsConnectColors.Error,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = { mainViewModel.refreshUser() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LetsConnectColors.Primary2
                        )
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
        
        is MainState.Success -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    MainTopAppBar(
                        currentRoute = selectedRoute,
                        userName = user?.name ?: "User",
                        onLogout = { mainViewModel.logout() }
                    )
                },
                bottomBar = {
                    MainBottomNavigationBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            ) { innerPadding ->
                MainNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    mainViewModel = mainViewModel
                )
            }
            
            LaunchedEffect(currentDestination?.route) {
                currentDestination?.route?.let { route ->
                    mainViewModel.onRouteSelected(route)
                }
            }
        }
        
        is MainState.LoggedOut -> {
            // This will trigger onLogout in LaunchedEffect above
        }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    currentRoute: String,
    userName: String,
    onLogout: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val title = when (currentRoute) {
        "home" -> "LetsConnect"
        "messages" -> "Messages"
        "contacts" -> "Contacts"
        "notifications" -> "Notifications"
        "profile" -> "Profile"
        else -> "LetsConnect"
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = LetsConnectColors.OnSurface
            )
        },
        actions = {
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Menu",
                        tint = LetsConnectColors.OnSurface
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Hello, $userName") },
                        onClick = { /* Navigate to profile */ }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {
                            showMenu = false
                            // Navigate to settings
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Help & Support") },
                        onClick = {
                            showMenu = false
                            // Navigate to support
                        }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            showMenu = false
                            onLogout()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LetsConnectColors.Surface,
            titleContentColor = LetsConnectColors.OnSurface
        )
    )
}
@Composable
fun MainBottomNavigationBar(
    navController: NavHostController,
    currentDestination: androidx.navigation.NavDestination?
) {
    NavigationBar(
        containerColor = LetsConnectColors.Surface,
        contentColor = LetsConnectColors.OnSurface,
        tonalElevation = 0.dp,
        modifier = Modifier

            .fillMaxWidth()
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label,maxLines = 1) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LetsConnectColors.Primary2,
                    selectedTextColor = LetsConnectColors.Primary2,
                    unselectedIconColor = LetsConnectColors.OnSurfaceVariant,
                    unselectedTextColor = LetsConnectColors.OnSurfaceVariant,
                    indicatorColor = LetsConnectColors.Primary2.copy(alpha = 0.1f)
                )
            )
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun MainBottomNavigationBarPreview() {
    MainBottomNavigationBar(
        navController = NavHostController(LocalContext.current),
        currentDestination = null

    )
}

@Composable
@Preview(showBackground = true)
fun MainTopAppBarPreview() {
    MainTopAppBar(
        currentRoute = "home",
        userName = "John Doe",
        onLogout = {}
    )
}*/

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    com.example.auth_v1.ui.theme.Auth_v1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MainTopAppBar(
                    currentRoute = "home",
                    userName = "John Doe",
                    onLogout = {}
                )
            },
            bottomBar = {
                MainBottomNavigationBar(
                    navController = rememberNavController(),
                    currentDestination = null
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Home Screen Content",
                    color = LetsConnectColors.OnSurface
                )
            }
        }
    }
}