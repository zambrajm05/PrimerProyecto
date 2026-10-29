package com.example.navegacionmvvm.model

data class Paciente(
    val id: String,
    val nombre: String,
    val raza: String,
    val edad: Int,
    val contactoEmergencia: String = "",
    val esUrgencia: Boolean = false
)