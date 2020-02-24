package com.example.gestionnairerapportdechantier.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "adresse")
data class Adresse (


    @ColumnInfo(name = "adresse_id")
    @PrimaryKey(autoGenerate = true)
    var adresseId: Int,
    var nom: String,

    @ColumnInfo(name = "numero_rue")
    var numeroRue: String,
    var rue: String,

    @ColumnInfo(name = "code_postal")
    var codePostal: String,
    var ville: String,
    var pays: String
)