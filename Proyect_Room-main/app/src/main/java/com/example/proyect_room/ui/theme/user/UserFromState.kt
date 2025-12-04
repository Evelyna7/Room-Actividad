package com.example.proyect_room.ui.user

data class UserFormState(
    val name: String = "",
    val age: String = "",
    val email: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
