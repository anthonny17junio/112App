package com.teltronic.app112.screens.medicalInfo


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.teltronic.app112.R
import com.teltronic.app112.database.room.DatabaseApp
import com.teltronic.app112.databinding.FragmentMedicalInfoBinding

/**
 * A simple [Fragment] subclass.
 */
class MedicalInfoFragment : Fragment() {

    private lateinit var binding: FragmentMedicalInfoBinding
    private lateinit var viewModel: MedicalInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_medical_info,
            container,
            false
        )

        //Inicializo el viewModel
        val application = requireNotNull(this.activity).application
        val dataSource = DatabaseApp.getInstance(application).medicalInfoDao //QUITAR ESTO DE AUÍ Y MOVERLO SOLO AL VIEW MODEL
        val viewModelFactory = MedicalInfoViewModelFactory(dataSource, application, binding)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MedicalInfoViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.medicalInfoViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        configureBackButton()
        configureSnackbarEventObserver()
        configureEnableInterfaceObserver()
        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)
        return binding.root
    }

    private fun configureBackButton() {
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_right_menu, menu)
    }

    //Navega al main fragment cuando se presiona el icono home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeIconItem -> {
                val actionNavigate =
                    MedicalInfoFragmentDirections.actionMedicalInfoFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }

    private fun configureSnackbarEventObserver() {
        viewModel.showSnackbarEvent.observe(
            this as LifecycleOwner, Observer { shouldShow ->
                if (shouldShow) {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.message_medical_information_saved),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.doneShowingSnackbar()
                }
            }
        )
    }

    private fun configureEnableInterfaceObserver() {
//        viewModel.boolEnableInterface.observe(
//            this as LifecycleOwner, Observer { isEnabled ->
//                if (isEnabled) { //Si la interfaz SI está habilitada NO se muestra el progress bar
//                    binding.progressBar.visibility = View.INVISIBLE
//                } else { //Si la interfaz está NO habilitada SI se muestra el progress bar (se está guardando en la base de datos)
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//            }
//        )
    }
}
