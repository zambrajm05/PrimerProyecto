package com.example.navegacionmvvm.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navegacionmvvm.model.Paciente
import com.example.navegacionmvvm.viewmodel.PacienteViewModel

/**
 * Pantalla principal: registra y muestra los pacientes de la clínica.
 *
 * El ViewModel se obtiene con `viewModel()`, así sobrevive a los cambios de
 * configuración (por ejemplo, girar el teléfono) sin perder los datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioView(
    viewModel: PacienteViewModel = viewModel()
) {
    // La vista "observa" el estado; al cambiar en el ViewModel, Compose recompone solo.
    val listaPacientes = viewModel.pacientes

    Scaffold(
        // Los insets los maneja el Scaffold de NavView, por eso aquí van en 0.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("KotlinPet Manager 🐾") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            FormularioPaciente(viewModel = viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pacientes registrados (${listaPacientes.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (listaPacientes.isEmpty()) {
                Text(
                    text = "Todavía no hay pacientes registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = listaPacientes, key = { it.id }) { paciente ->
                        TarjetaPaciente(
                            paciente = paciente,
                            onEliminar = { viewModel.eliminarPaciente(paciente.id) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Formulario de registro. No guarda estado propio: todo se lee y se escribe
 * en el ViewModel.
 */
@Composable
private fun FormularioPaciente(viewModel: PacienteViewModel) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Registrar nuevo paciente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre") },
                placeholder = { Text("Ej. Firulais") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.especie,
                    onValueChange = viewModel::actualizarEspecie,
                    label = { Text("Especie / Raza") },
                    placeholder = { Text("Ej. Perro (Beagle)") },
                    singleLine = true,
                    modifier = Modifier.weight(2f)
                )
                OutlinedTextField(
                    value = viewModel.edad,
                    onValueChange = viewModel::actualizarEdad,
                    label = { Text("Edad") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = viewModel.contacto,
                onValueChange = viewModel::actualizarContacto,
                label = { Text("Contacto de emergencia") },
                placeholder = { Text("Ej. 987 654 321") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = viewModel.esUrgencia,
                    onCheckedChange = viewModel::actualizarEsUrgencia
                )
                Text(
                    text = "Requiere atención inmediata (urgencia)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            viewModel.mensajeError?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.agregarPaciente() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Registrar Paciente")
                }
                OutlinedButton(onClick = { viewModel.limpiarFormulario() }) {
                    Text("Limpiar")
                }
            }
        }
    }
}

/** Tarjeta que representa a un paciente dentro de la LazyColumn. */
@Composable
private fun TarjetaPaciente(
    paciente: Paciente,
    onEliminar: () -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = paciente.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = paciente.raza,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (paciente.esUrgencia) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Urgencia",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Urgencia",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar paciente",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Edad: ${paciente.edad} años",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Contacto: ${paciente.contactoEmergencia}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}