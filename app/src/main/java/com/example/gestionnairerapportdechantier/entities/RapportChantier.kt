package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairerapportdechantier.entities.Chantier
import java.util.*


@Entity(
    tableName = "rappport_chantier",

    foreignKeys = arrayOf(
        ForeignKey(
            entity = Chantier::class,
            parentColumns = arrayOf("chantier_id"),
            childColumns = arrayOf("chantier_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Personnel::class,
            parentColumns = arrayOf("personnel_id"),
            childColumns = arrayOf("responsable_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class RapportChantier(

    @ColumnInfo(name = "rapport_chantier_id")
    @PrimaryKey(autoGenerate = true)
    var rapportChantierId: Int? = null,

    @ColumnInfo(name = "chantier_id")
    var chantierId: Int? = null,

    @ColumnInfo(name = "responsable_id")
    var ReponsableId: Int? = null,

    @ColumnInfo(name = "debut_rapport_chantier")
    var debutRapportChantier: String? = null,

    @ColumnInfo(name = "fin_rapport_chantier")
    var finRapportChantier: String? = null


)