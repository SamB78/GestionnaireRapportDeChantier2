package com.example.gestionnairerapportdechantier.entities

import androidx.room.*
import java.time.LocalDate
import java.util.*

@Entity(tableName = "materiel")
data class Materiel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var marque: String? = null,
    var modele: String? = null,
    var numeroSerie: String? = null,
    var type: String? = null,
    var enService: Boolean = true,
    var materielEntretien: Boolean = true,
    var materielChantier: Boolean = false,
    var miseEnCirculation: LocalDate? = null,
    var urlPictureMateriel: String? = null,
    @Ignore
    var isChecked: Boolean = false,
    @Ignore
    var nombreHeuresUtilisees: Int = 0

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
    @ColumnInfo(name = "nb_heures_utilisees")
    var NbHeuresUtilisees: Int = 0
)

data class TypeMateriel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var typeMateriel: String

)


