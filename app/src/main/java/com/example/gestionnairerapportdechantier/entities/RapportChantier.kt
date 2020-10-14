package com.example.gestionnairerapportdechantier.entities

import androidx.room.*
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
    @ColumnInfo(name = "chef_chantier_id")
    var chefChantierId: Int? = null,
    @ColumnInfo(name = "date_rapport_chantier")
    var dateRapportChantier: LocalDate? = null,
    @Embedded
    var meteo: Meteo = Meteo(),
    @Embedded
    var infosRapportChantier: InfosRapportChantier = InfosRapportChantier(),

    var observations: String? = null,

    var totalMOPersonnel: Int = 0,
    var totalMOInterimaire: Int = 0,
    var totalMO: Int = 0,
    var totalHeuresMaterielSociete: Int = 0,
    var totalHeuresMaterielLocation: Int = 0,
    var totalHeuresMateriel: Int = 0,
    var totalMateriaux: Int = 0,
    var totalSousTraitant: Int = 0,
    var totalRapportChantier: Int = 0


)

data class InfosRapportChantier(

    var securiteRespectPortEPI: Boolean = false,

    var securiteBalisage: Boolean = false,

    var environnementProprete: Boolean = false,

    var environnementNonPollution: Boolean = false,

    var propreteVehicule: Boolean = false,

    var entretienMateriel: Boolean = false,

    var renduCarnetDeBord: Boolean = false,

    var renduBonCarburant: Boolean = false,

    var renduBonDecharge: Boolean = false,

    var feuillesInterimaires: Boolean = false,

    var bonDeCommande: Boolean = false

)