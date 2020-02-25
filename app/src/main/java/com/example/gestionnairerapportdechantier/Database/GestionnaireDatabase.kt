package com.example.gestionnairerapportdechantier.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestionnairerapportdechantier.entities.*

@Database(
    entities = [Chantier::class,
        Adresse::class,
        Personnel::class,
        AssociationPersonnelRapportJournalierChantier::class,
        RapportJournalierChantier::class,
        RapportChantier::class],
    version = 3, exportSchema = false)

abstract class GestionnaireDatabase : RoomDatabase() {

    abstract val ChantierDao: ChantierDao
    abstract val PersonnelDao: PersonnelDao

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