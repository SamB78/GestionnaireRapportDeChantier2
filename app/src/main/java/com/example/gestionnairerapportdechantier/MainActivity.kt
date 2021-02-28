package com.example.gestionnairerapportdechantier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gestionnairerapportdechantier.databinding.ActivityMainBinding
import com.example.gestionnairerapportdechantier.personnel.gestionPersonnel.GestionPersonnelViewModel
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelFragment
import com.example.gestionnairerapportdechantier.personnel.listePersonnel.ListePersonnelFragmentDirections
import com.google.android.material.navigation.NavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val viewModel: MainActivityViewModel by viewModels()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        );

        Timber.plant(Timber.DebugTree())
        setupNavigation()

    }


    /**
     * Called when the hamburger menu or back button are pressed on the Toolbar
     *
     * Delegate this to Navigation.
     */
    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(findNavController(R.id.navHostFragment), binding.drawerLayout)


    private fun setupNavigation() {
        // first find the nav controller
        val navController = findNavController(R.id.navHostFragment)
        setSupportActionBar(binding.toolbar)
        // then setup the action bar, tell it about the DrawerLayout
        setupActionBarWithNavController(navController, binding.drawerLayout)

        findViewById<NavigationView>(R.id.activity_main_nav_view)
            .setupWithNavController(navController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        Timber.i("TEST")
        return when (item.itemId) {
            R.id.itemPersonnel -> {
                Timber.i("itemPersonnel")
                val action = ListePersonnelFragmentDirections.actionGlobalListePersonnelFragment()
                findNavController(R.id.navHostFragment).navigate(action)
                true
            }
            R.id.itemMateriel -> {
                true
            }
            else -> {
                false
            }
        }
    }

}
