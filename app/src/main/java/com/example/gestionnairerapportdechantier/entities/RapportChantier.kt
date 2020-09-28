package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairerapportdechantier.entities.Chantier
import java.time.LocalDate
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
            childColumns = arrayOf("chef_chantier_id"),
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
    @ColumnInfo(name="chef_chantier_id")
    var chefChantierId: Int? = null,
    @ColumnInfo(name = "date_rapport_chantier")
    var dateRapportChantier: LocalDate? = null,
    var meteo: String? = null,
    var observations: String? = null,

    @ColumnInfo(name = "securite_respect_port_epi")
    var securiteRespectPortEPI: Boolean? = null,

    @ColumnInfo(name = "securite_balisage")
    var securiteBalisage: Boolean? = null,

    @ColumnInfo(name = "environnement_proprete")
    var environnementProprete: Boolean? = null,

    @ColumnInfo(name = "environnement_non_pollution")
    var environnementNonPollution: Boolean? = null





)