package com.example.gestionnairerapportdechantier.entities

data class Meteo(
    var soleil: Boolean = false,
    var pluie: Boolean = false,
    var vent: Boolean = false,
    var gel: Boolean = false,
    var neige: Boolean = false
)