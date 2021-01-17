package com.example.gestionnairerapportdechantier.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(tableName = "vehicule")
data class Vehicule (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var marque: String? = null,
    var modele:String? = null,
    var immatriculation: String? = null,
    var type: String? = null,
    var miseEnCirculation: LocalDate? = null,
    var urlPictureVehicule : String? = null
)