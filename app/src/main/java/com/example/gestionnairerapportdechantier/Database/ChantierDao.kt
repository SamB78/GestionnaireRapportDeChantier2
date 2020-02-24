package com.example.gestionnairerapportdechantier.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.Chantier

@Dao
interface ChantierDao {

    @Insert
    fun insert(chantier: Chantier)

    @Update
    fun update(chantier: Chantier)


}