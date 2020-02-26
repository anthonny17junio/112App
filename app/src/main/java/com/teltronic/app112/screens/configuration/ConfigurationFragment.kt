package com.teltronic.app112.screens.configuration


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.classes.Preferences
import com.teltronic.app112.databinding.FragmentConfigurationBinding

/**
 * A simple [Fragment] subclass.
 */
class ConfigurationFragment : Fragment() {

    private lateinit var binding: FragmentConfigurationBinding
    private lateinit var viewModel: ConfigurationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_configuration,
            container,
            false
        )
        //Inicializo el viewModel
        viewModel =
            ViewModelProvider(this).get(ConfigurationViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.configurationViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        configureBtnSaveObserver()
        configureBackButton()
        return binding.root
    }

    private fun configureBtnSaveObserver() {
        viewModel.boolSave.observe(
            this as LifecycleOwner,
            Observer { shouldSave ->
                if (shouldSave) {
                    //TODO: analizar donde debería ir este código
                    val newLanguage = binding.spinnerLanguage.selectedItem.toString()
                    val langCode = Preferences.getLangCode(newLanguage, activity as Context)

                    Preferences.setLocate(langCode, activity as Context)
                    viewModel.saveComplete()
                    activity!!.recreate()
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.changes_saved),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
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
                val actionNavigate = ConfigurationFragmentDirections.actionConfigurationFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
