package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "personnel")
data class Personnel (

    @ColumnInfo(name = "personnel_id")
    @PrimaryKey(autoGenerate = true)
    var personnelId: Int,
    var nom: String,
    var prenom: String,
    var Fonction: Int
)

@Entity(tableName = "assocation_personnel_rapport_joutnalier_chantier",
    foreignKeys =
        arrayOf(
            ForeignKey(
                entity = Personnel::class,
                parentColumns = arrayOf("personnel_id"),
                childColumns = arrayOf("personnel_id"),
                onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                entity = RapportJournalierChantier::class,
                parentColumns = arrayOf("rapport_journalier_chantier_id"),
                childColumns = arrayOf("rapport_journalier_chantier_id"),
                onDelete = ForeignKey.CASCADE
            )
        )
)
data class AssociationPersonnelRapportJournalierChantier(

    @PrimaryKey(autoGenerate = true)
    var associationId: Int,
    @ColumnInfo(name = "personnel_id")
    var personnelId: Int,
    @ColumnInfo(name = "rapport_journalier_chantier_id")
    var rapportJournalierChantierID: Int,
    @ColumnInfo(name = "nb_heures_travaillees")
    var NbHeuresTravaillees: Float
)