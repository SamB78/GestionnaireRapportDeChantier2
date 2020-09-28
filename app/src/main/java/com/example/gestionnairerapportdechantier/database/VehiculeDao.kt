package com.example.gestionnairerapportdechantier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gestionnairerapportdechantier.entities.Vehicule

@Dao
interface VehiculeDao {

    @Insert
    fun insertVehicule(vehicule: Vehicule): Long

    @Update
    fun updateVehicule(vehicule: Vehicule)

    @Query("SELECT * FROM vehicule")
    fun getAllFromVehicules(): LiveData<List<Vehicule>>


    @Query("SELECT * FROM vehicule WHERE id  = :id")
    fun getVehiculeById(id: Long): Vehicule

}