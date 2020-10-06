package com.example.gestionnairerapportdechantier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gestionnairerapportdechantier.entities.*


@Database(
    entities = [Chantier::class,
        Personnel::class,
        AssociationPersonnelRapportChantier::class,
        RapportChantier::class,
        AssociationPersonnelChantier::class,
        AssociationMaterielRapportChantier::class,
        Vehicule::class,
        Materiel::class],
    version = 28, exportSchema = false
)

@TypeConverters(LocalDateTimeConverter::class)

abstract class GestionnaireDatabase : RoomDatabase() {

    abstract val chantierDao: ChantierDao
    abstract val personnelDao: PersonnelDao
    abstract val vehiculeDao: VehiculeDao
    abstract val materielDao: MaterielDao
    abstract val rapportChantierDao: RapportChantierDao
    abstract val associationPersonnelChantierDao: AssociationPersonnelChantierDao
    abstract val associationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao
    abstract val associationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao


    companion object {

        @Volatile
        private var INSTANCE: GestionnaireDatabase? = null

        fun getInstance(context: Context): GestionnaireDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GestionnaireDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }

        }
    }
}