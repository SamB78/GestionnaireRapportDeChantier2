package com.example.gestionnairerapportdechantier.rapportChantier.affichageRapportChantier

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.example.gestionnairerapportdechantier.R
import com.example.gestionnairerapportdechantier.database.GestionnaireDatabase
import com.example.gestionnairerapportdechantier.databinding.FragmentAffichageDetailsRapportChantierBinding
import timber.log.Timber


class AffichageDetailsRapportChantierFragment : Fragment() {

    private val PERMISSION_CODE = 1000


    lateinit var viewModelFactory: AffichageDetailsRapportChanterViewModelFactory

    val viewModel: AffichageDetailsRapportChantierViewModel by navGraphViewModels(R.id.AffichageRapportsChantierNavGraph) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_affichage_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            val uri = viewModel.uri
            val cR = context!!.contentResolver
            Timber.i("uri = ${uri.toString()}, mime = ${cR.getType(uri)}")

            uri?.let {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(uri, cR.getType(uri))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }

//            val shareIntent = ShareCompat.IntentBuilder.from(this)
//                .setText("Share PDF doc")
//                .setType("application/vnd.google-apps.spreadsheet")
//                .setStream(uri)
//                .intent
//                .setPackage("com.google.android.apps.docs")
//            startActivity(shareIntent)


            true
        }
        else -> {
            super.onOptionsItemSelected(item)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSourceRapportChantier =
            GestionnaireDatabase.getInstance(application).rapportChantierDao
        val dataSourceChantier = GestionnaireDatabase.getInstance(application).chantierDao
        val dataSourceMateriel = GestionnaireDatabase.getInstance(application).materielDao
        val dataSourceMaterielLocation =
            GestionnaireDatabase.getInstance(application).materielLocationDao
        val dataSourceMateriaux = GestionnaireDatabase.getInstance(application).materiauxDao
        val dataSourceSousTraitance = GestionnaireDatabase.getInstance(application).sousTraitanceDao

        val dataSourceAssociationPersonnelChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelChantierDao
        val dataSourcePersonnel = GestionnaireDatabase.getInstance(application).personnelDao
        val dataSourceAssociationPersonnelRapportChantier =
            GestionnaireDatabase.getInstance(application).associationPersonnelRapportChantierDao
        val dataSourceAssociationMaterielRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMaterielRapportChantierDao
        val dataSourceAssociationMaterielLocationRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMaterielLocationRapportChantierDao
        val dataSourceAssociationMateriauxRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationMateriauxRapportChantierDao
        val dataSourceAssociationSousTraitanceRapportChantierDao =
            GestionnaireDatabase.getInstance(application).associationSousTraitanceRapportChantierDao

        val idChantier = AffichageDetailsRapportChantierFragmentArgs.fromBundle(arguments!!).idChantier
        val dateDebut = AffichageDetailsRapportChantierFragmentArgs.fromBundle(arguments!!).dateDebut
        val dateFin = AffichageDetailsRapportChantierFragmentArgs.fromBundle(arguments!!).dateFin

        viewModelFactory = AffichageDetailsRapportChanterViewModelFactory(
            application,
            dataSourceRapportChantier,
            dataSourceChantier,
            dataSourcePersonnel,
            dataSourceMateriel,
            dataSourceMaterielLocation,
            dataSourceMateriaux,
            dataSourceSousTraitance,
            dataSourceAssociationPersonnelChantier,
            dataSourceAssociationPersonnelRapportChantier,
            dataSourceAssociationMaterielRapportChantierDao,
            dataSourceAssociationMaterielLocationRapportChantierDao,
            dataSourceAssociationMateriauxRapportChantierDao,
            dataSourceAssociationSousTraitanceRapportChantierDao,
            -1L,
            idChantier.toInt(),
            dateDebut,
            dateFin
        )


        val binding =
            FragmentAffichageDetailsRapportChantierBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel


        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun generateExcelFile(): Uri? {
        Timber.i("onClickGenerateXlsxFile fragment")
        //if system os is Marshmallow or Above, we need to request runtime permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            Timber.i("onClickGenerateXlsxFile fragment permission NOT granted")
            //permission was not enabled
            val permission = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            //show popup to request permission
            requestPermissions(permission, PERMISSION_CODE)
        } else {
            //permission already granted
            Timber.i("onClickGenerateXlsxFile fragment permission granted")
            val uri = viewModel.onClickGenerateXlsxFile()
            return uri
        }

        return null
    }

}