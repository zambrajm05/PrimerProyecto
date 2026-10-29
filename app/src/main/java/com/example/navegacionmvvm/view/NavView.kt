package com.example.navegacionmvvm.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navegacionmvvm.navigation.ItemsMenu

/**
 * Contenedor de navegación: barra inferior + NavHost.
 *
 * Cada pantalla define su propio Scaffold con su TopAppBar, por eso este
 * Scaffold solo aporta la barra inferior.
 */
@Composable
fun NavView() {
    val navController = rememberNavController()

    val navegacionItems = listOf(
        ItemsMenu.inicio,
        ItemsMenu.citas,
        ItemsMenu.Ajustes
    )

    Scaffold(
        // Los insets (barra de estado) los aplica el Scaffold de cada pantalla.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                // Obtenemos la ruta actual de la navegación
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rutaActual = navBackStackEntry?.destination?.route

                navegacionItems.forEach { item ->
                    NavigationBarItem(
                        selected = rutaActual == item.ruta,
                        onClick = {
                            navController.navigate(item.ruta) {
                                // Evita acumular pantallas en la pila de navegación
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = item.icono, contentDescription = item.titulo)
                        },
                        label = { Text(item.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ItemsMenu.inicio.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ItemsMenu.inicio.ruta) {
                PantallaInicioView()
            }
            composable(ItemsMenu.citas.ruta) {
                PantallaCitasView()
            }
            composable(ItemsMenu.Ajustes.ruta) {
                PantallaConfigView()
            }
        }
    }
}