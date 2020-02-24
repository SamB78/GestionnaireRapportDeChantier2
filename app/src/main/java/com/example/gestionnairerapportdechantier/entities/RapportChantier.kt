package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairerapportdechantier.entities.Chantier
import java.util.*


@Entity(tableName = "rappport_chantier",

    foreignKeys = arrayOf(ForeignKey(entity = Chantier::class,
        parentColumns = arrayOf("chantier_id"),
        childColumns = arrayOf("chantier_id"),
        onDelete = ForeignKey.CASCADE)
    )
)
data class RapportChantier (

    @ColumnInfo(name = "rapport_chantier_id")
    @PrimaryKey(autoGenerate = true)
    var rapportChantierId: Int,

    @ColumnInfo(name = "chantier_id")
    var chantierId: Int,

    @ColumnInfo(name = "debut_rapport_chantier")
    var debutRapportChantier: String,

    @ColumnInfo(name = "fin_rapport_chantier")
    var finRapportChantier: String,

    @ColumnInfo(name = "securite_respect_port_epi")
    var securiteRespectPortEPI: Boolean,

    @ColumnInfo(name = "securite_balisage")
    var securiteBalisage: Boolean,

    @ColumnInfo(name = "environnement_proprete")
    var environnementProprete: Boolean,

    @ColumnInfo(name = "environnement_non_pollution")
    var environnementNonPollution: Boolean
)