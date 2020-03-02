package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.gestionnairerapportdechantier.entities.Adresse

@Entity(
    tableName = "chantier",

    foreignKeys = arrayOf(
        ForeignKey(
            entity = Adresse::class,
            parentColumns = arrayOf("adresse_id"),
            childColumns = arrayOf("adresse_chantier"),
            onDelete = ForeignKey.SET_DEFAULT
        )
    )
)

data class Chantier(

    @ColumnInfo(name = "chantier_id")
    @PrimaryKey(autoGenerate = true)
    var chantierId: Int? = null,

    @ColumnInfo(name = "numero_chantier")
    var numeroChantier: Int? = null,

    @ColumnInfo(name = "nom_chantier")
    var nomChantier: String? = null,

    @ColumnInfo(name = "adresse_chantier")
    var adresseChantier: Int? = null
)