package com.example.gestionnairerapportdechantier.entities

import androidx.room.*
import com.example.gestionnairerapportdechantier.entities.Chantier
import timber.log.Timber
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
    var totalSousTraitance: Int = 0,
    var totalRapportChantier: Int = 0,
    var entretien: Boolean = false,
    var chantier: Boolean = false,

    @Embedded
    var dataSaved: DataSaved = DataSaved()



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

) {
    fun sendNumberOfTrueChamps(): Int {
        var number = 0
        if (securiteRespectPortEPI) number += 1
        if (securiteBalisage) number += 1
        if (environnementProprete) number += 1
        if (environnementNonPollution) number += 1
        if (propreteVehicule) number += 1
        if (entretienMateriel) number += 1
        if (renduCarnetDeBord) number += 1
        if (renduBonCarburant) number += 1
        if (renduBonDecharge) number += 1
        if (feuillesInterimaires) number += 1
        if (bonDeCommande) number += 1
        Timber.i("Numbe value : $number")
        return number
    }

    fun sendNumberOfFalseChamps(): Int {
        return 11 - sendNumberOfTrueChamps()

    }
}

data class DataSaved(
    var dataPersonnel: Boolean = false,
    var dataMateriel: Boolean = false,
    var dataMaterielLocation: Boolean = false,
    var dataMateriaux: Boolean = false,
    var dataSousTraitance: Boolean = false,
    var dataConformiteChantier: Boolean = false,
    var dataObservations: Boolean = false
)