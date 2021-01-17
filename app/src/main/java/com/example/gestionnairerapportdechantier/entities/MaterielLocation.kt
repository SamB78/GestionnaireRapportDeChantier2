package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "materiel_location")
data class MaterielLocation(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var fournisseur: String = "",
    var designation: String = "",
    var quantite: Int = 0
)

@Entity(
    tableName = "assocation_materiel_location_rapport_chantier",
    foreignKeys =
    [ForeignKey(
        entity = MaterielLocation::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("materiel_location_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RapportChantier::class,
        parentColumns = arrayOf("rapport_chantier_id"),
        childColumns = arrayOf("rapport_chantier_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationMaterielLocationRapportChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int? = null,
    @ColumnInfo(name = "materiel_location_id")
    var materielLocationId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierID: Int,
    @ColumnInfo(name = "nb_heures_utilisees")
    var NbHeuresUtilisees: Int = 0
)
