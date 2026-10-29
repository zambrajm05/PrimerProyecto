package com.example.navegacionmvvm.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ItemsMenu(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
) {
    object inicio: ItemsMenu("inicio", "Pacientes", Icons.Default.Home)
    object citas: ItemsMenu("Citas", "Citas", Icons.Default.DateRange)
    object Ajustes: ItemsMenu("Ajustes", "Ajustes", Icons.Default.Settings)
 }