package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairerapportdechantier.entities.RapportChantier
import java.util.*


@Entity(
    tableName = "rapport_journalier_chantier",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RapportChantier::class,
            parentColumns = arrayOf("rapport_chantier_id"),
            childColumns = arrayOf("rapport_chantier_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class RapportJournalierChantier(

    @ColumnInfo(name = "rapport_journalier_chantier_id")
    @PrimaryKey(autoGenerate = true)
    var rapportJournalierChantierId: Int,
    @ColumnInfo(name = "rapport_chantier_id")
    var rapportChantierId: Int,
    @ColumnInfo(name = "date_rapport_journalier_chantier")
    var dateRapportJournalierChantier: String,
    var meteo: String,
    var observations: String,

    @ColumnInfo(name = "securite_respect_port_epi")
    var securiteRespectPortEPI: Boolean,

    @ColumnInfo(name = "securite_balisage")
    var securiteBalisage: Boolean,

    @ColumnInfo(name = "environnement_proprete")
    var environnementProprete: Boolean,

    @ColumnInfo(name = "environnement_non_pollution")
    var environnementNonPollution: Boolean
)