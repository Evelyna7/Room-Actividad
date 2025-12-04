package com.example.proyect_room.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyect_room.data.local.UserEntity
import com.example.proyect_room.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UserViewModel:
 * - Orquesta la lógica entre la UI (Compose) y el UserRepository.
 * - Mantiene:
 *   - Estado del formulario.
 *   - Lista de usuarios almacenados en Room.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // --- Estado del formulario ---
    private val _formState = MutableStateFlow(UserFormState())
    val formState: StateFlow<UserFormState> = _formState.asStateFlow()

    // --- Lista de usuarios (estado derivado de Room) ---
    // Usamos stateIn para convertir el Flow del repositorio en un StateFlow.
    val users: StateFlow<List<UserEntity>> =
        userRepository.getAllUsers()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /**
     * Actualiza el campo name del formulario.
     */
    fun onNameChange(newName: String) {
        _formState.value = _formState.value.copy(
            name = newName,
            // Si estamos editando, limpiamos mensajes previos
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Actualiza el campo age del formulario.
     * Nota: es String porque viene de un TextField.
     */
    fun onAgeChange(newAge: String) {
        _formState.value = _formState.value.copy(
            age = newAge,
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Actualiza el campo email del formulario.
     */
    fun onEmailChange(newEmail: String) {
        _formState.value = _formState.value.copy(
            email = newEmail,
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Intenta guardar el usuario en la BD.
     * - Valida los campos.
     * - Convierte la edad a Int.
     * - Llama al repositorio usando coroutines.
     */
    fun saveUser() {
        val currentState = _formState.value

        // Validación básica de campos
        if (currentState.name.isBlank() ||
            currentState.age.isBlank() ||
            currentState.email.isBlank()
        ) {
            _formState.value = currentState.copy(
                errorMessage = "Todos los campos son obligatorios",
                successMessage = null
            )
            return
        }

        // Validar que age sea un Int válido
        val ageInt = currentState.age.toIntOrNull()
        if (ageInt == null || ageInt <= 0) {
            _formState.value = currentState.copy(
                errorMessage = "La edad debe ser un número entero positivo",
                successMessage = null
            )
            return
        }

        // (Opcional) Validación simple de email
        if (!currentState.email.contains("@")) {
            _formState.value = currentState.copy(
                errorMessage = "Correo electrónico no válido",
                successMessage = null
            )
            return
        }

        // Si todo está bien, lanzamos la inserción en una coroutine
        viewModelScope.launch {
            try {
                _formState.value = currentState.copy(
                    isSaving = true,
                    errorMessage = null,
                    successMessage = null
                )

                val user = UserEntity(
                    name = currentState.name,
                    age = ageInt,
                    email = currentState.email
                )

                userRepository.insertUser(user)

                // Limpiamos el formulario tras el guardado
                _formState.value = UserFormState(
                    successMessage = "Usuario guardado correctamente"
                )
            } catch (e: Exception) {
                // En un caso real, podrías loguear el error
                _formState.value = currentState.copy(
                    isSaving = false,
                    errorMessage = "Error al guardar el usuario: ${e.message}",
                    successMessage = null
                )
            } finally {
                // Aseguramos que isSaving vuelva a false
                _formState.value = _formState.value.copy(
                    isSaving = false
                )
            }
        }
    }

    /**
     * Limpia el formulario.
     * Útil si quieres agregar un botón de "Limpiar".
     */
    fun resetForm() {
        _formState.value = UserFormState()
    }
}
