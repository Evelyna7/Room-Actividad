package com.example.proyect_room


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase de aplicación principal.
 *
 * - La anotación @HiltAndroidApp le dice a Hilt:
 *   "Aquí empieza la app, prepárame el contenedor de dependencias".
 *
 * - Hilt genera por debajo una clase que extiende de Application
 *   y se encargará de crear y mantener el grafo de dependencias
 *   durante todo el ciclo de vida de la app.
 */
@HiltAndroidApp
class App : Application()
