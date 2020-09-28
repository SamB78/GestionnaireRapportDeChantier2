package com.example.gestionnairerapportdechantier.entities

import androidx.room.*


@Entity(tableName = "personnel")
data class Personnel(

    @ColumnInfo(name = "personnel_id")
    @PrimaryKey(autoGenerate = true)
    var personnelId: Int? = null,
    var nom: String? = null,
    var prenom: String? = null,
    var numContact: String? = null,
    var mailContact: String? = null,
    var chefEquipe: Boolean = false,
    var interimaire: Boolean = false,
    var urlPicturepersonnel: String? = null,
    @Ignore
    var isChecked: Boolean = false,
    @Ignore
    var nombreHeuresTravaillees: Int = 0
)

@Entity(
    tableName = "assocation_personnel_rapport_chantier",
    foreignKeys =
    [ForeignKey(
        entity = Personnel::class,
        parentColumns = arrayOf("personnel_id"),
        childColumns = arrayOf("personnel_id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = RapportChantier::class,
        parentColumns = arrayOf("rapport_chantier_id"),
        childColumns = arrayOf("rapport_chantier_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationPersonnelRapportChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int? = null,
    @ColumnInfo(name = "personnel_id")
    var personnelId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierID: Int,
    @ColumnInfo(name = "nb_heures_travaillees")
    var NbHeuresTravaillees: Int = 0
)

@Entity(
    primaryKeys = ["personnelID","chantierID"],
    foreignKeys =
    [ForeignKey(
        entity = Personnel::class,
        parentColumns = arrayOf("personnel_id"),
        childColumns = arrayOf("personnelID"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Chantier::class,
        parentColumns = arrayOf("chantier_id"),
        childColumns = arrayOf("chantierID"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class AssociationPersonnelChantier(

    var personnelID: Int,
    var chantierID: Int

)
