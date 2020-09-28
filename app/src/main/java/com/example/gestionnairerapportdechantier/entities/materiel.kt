package com.example.gestionnairerapportdechantier.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "materiel")
data class Materiel (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var marque: String? = null,
    var modele:String? = null,
    var numeroSerie: String? = null,
    var type: String? = null,
    var miseEnCirculation: LocalDate? = null,
    var urlPictureMateriel : String? = null
)