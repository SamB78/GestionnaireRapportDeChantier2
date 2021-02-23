package com.example.gestionnairerapportdechantier.entities

import androidx.room.*
import com.example.gestionnairerapportdechantier.entities.Adresse

@Entity(
    tableName = "chantier"

//    ,
//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = Adresse::class,
//            parentColumns = arrayOf("adresse_id"),
//            childColumns = arrayOf("adresse_chantier"),
//            onDelete = ForeignKey.SET_DEFAULT
//        )
//    )
)

data class Chantier(

    @ColumnInfo(name = "chantier_id")
    @PrimaryKey(autoGenerate = true)
    var chantierId: Int? = null,

    @ColumnInfo(name = "numero_chantier")
    var numeroChantier: String? = null,

    @ColumnInfo(name = "nom_chantier")
    var nomChantier: String? = null,

    @Embedded
    var adresseChantier: Adresse = Adresse(),
    var urlPictureChantier: String? = null,
    var identiteResponsableSite: String? = null,
    var numContactResponsableSite: String? = null,
    var mailContactResponsableSite: String? = null,
    var chefChantierId: Int? = null,
    var typeChantier: Int = 1 // 1 = Chantier 2 = Entretien

    )