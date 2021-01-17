package com.example.gestionnairerapportdechantier.entities

import androidx.room.*

@Entity(tableName = "materiaux")
data class Materiaux (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var fournisseur: String = "",
    var description: String = "",
    var nDeBon: String = "",
    @Ignore
    var quantite: Int = 0
)



@Entity(
    tableName = "assocation_materiaux_rapport_chantier",
    foreignKeys =
    [ForeignKey(
        entity = Materiaux::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("materiaux_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RapportChantier::class,
        parentColumns = arrayOf("rapport_chantier_id"),
        childColumns = arrayOf("rapport_chantier_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationMateriauxRapportChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int? = null,
    @ColumnInfo(name = "materiaux_id")
    var materiauxId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierID: Int,
    @ColumnInfo(name = "nb_heures_travaillees")
    var quantite: Int = 0
)