package com.example.gestionnairerapportdechantier.entities


data class Adresse (
    var rue: String? = null,
    var codePostal: String? = null,
    var ville: String? = null,
    var pays: String? = "France"
)