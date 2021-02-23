package com.example.gestionnairerapportdechantier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.gestionnairerapportdechantier.entities.*
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker


@Database(
    entities = [Chantier::class,
        Personnel::class,
        AssociationPersonnelRapportChantier::class,
        RapportChantier::class,
        TraitementPhytosanitaire::class,
        AssociationPersonnelChantier::class,
        AssociationMaterielRapportChantier::class,
        AssociationMaterielLocationRapportChantier::class,
        AssociationMateriauxRapportChantier::class,
        AssociationSousTraitanceRapportChantier::class,
        Vehicule::class,
        Materiel::class,
        MaterielLocation::class,
        Materiaux::class,
        SousTraitance::class,
        TacheEntretien::class,
        AssociationTacheEntretienRapportChantier::
        class
    ],
    version = 48, exportSchema = false
)

@TypeConverters(LocalDateTimeConverter::class)

abstract class GestionnaireDatabase : RoomDatabase() {

    abstract val chantierDao: ChantierDao
    abstract val personnelDao: PersonnelDao
    abstract val vehiculeDao: VehiculeDao
    abstract val materielDao: MaterielDao
    abstract val materielLocationDao: MaterielLocationDao
    abstract val materiauxDao: MateriauxDao
    abstract val sousTraitanceDao: SousTraitanceDao
    abstract val rapportChantierDao: RapportChantierDao
    abstract val associationPersonnelChantierDao: AssociationPersonnelChantierDao
    abstract val associationPersonnelRapportChantierDao: AssociationPersonnelRapportChantierDao
    abstract val associationMaterielRapportChantierDao: AssociationMaterielRapportChantierDao
    abstract val associationMaterielLocationRapportChantierDao: AssociationMaterielLocationRapportChantierDao
    abstract val associationMateriauxRapportChantierDao: AssociationMateriauxRapportChantierDao
    abstract val associationSousTraitanceRapportChantierDao: AssociationSousTraitanceRapportChantierDao


    companion object {

        @Volatile private var instance: GestionnaireDatabase? = null

        fun getInstance(context: Context): GestionnaireDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GestionnaireDatabase {
            return Room.databaseBuilder(context, GestionnaireDatabase::class.java, "gestionnaire_chantier_database")
                .fallbackToDestructiveMigration()
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }

    }
}