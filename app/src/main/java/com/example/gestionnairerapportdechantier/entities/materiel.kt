package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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

@Entity(
    tableName = "assocation_materiel_rapport_chantier",
    foreignKeys =
    [ForeignKey(
        entity = Materiel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("materiel_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RapportChantier::class,
        parentColumns = arrayOf("rapport_chantier_id"),
        childColumns = arrayOf("rapport_chantier_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationMaterielRapportChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int? = null,
    @ColumnInfo(name = "materiel_id")
    var materielId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierID: Int,
    @ColumnInfo(name = "nb_heures_travaillees")
    var NbHeuresTravaillees: Int = 0
)