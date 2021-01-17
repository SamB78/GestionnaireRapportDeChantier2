package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "sous_traitance")
data class SousTraitance (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var societe: String = "",
    var prestations: String = "",
    var quantite: Int = 0
        )

@Entity(
    tableName = "assocation_sous_traitance_rapport_chantier",
    foreignKeys =
    [ForeignKey(
        entity = SousTraitance::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("sous_traitance_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RapportChantier::class,
        parentColumns = arrayOf("rapport_chantier_id"),
        childColumns = arrayOf("rapport_chantier_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationSousTraitanceRapportChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int? = null,
    @ColumnInfo(name = "sous_traitance_id")
    var sousTraitanceId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierID: Int,
    var quantite: Int = 0
)