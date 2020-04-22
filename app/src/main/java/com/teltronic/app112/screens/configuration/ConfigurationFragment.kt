package com.teltronic.app112.screens.configuration


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentConfigurationBinding

/**
 * A simple [Fragment] subclass.
 */
class ConfigurationFragment : Fragment() {

    lateinit var binding: FragmentConfigurationBinding
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
        viewModel = ViewModelProvider(this).get(ConfigurationViewModel::class.java)
        viewModel.loadConfigurations(this.requireActivity())

        //"Uno" el layout con esta clase por medio del binding
        binding.configurationViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        configureBtnSaveObserver()
        configureBackButton()
        configurePosSelectedLanguageObserver()
        return binding.root
    }

    private fun configurePosSelectedLanguageObserver() {
        viewModel.posSelectedLanguage.observe(
            this as LifecycleOwner,
            Observer { posLang ->
                if (posLang != -1) {
                    binding.spinnerLanguage.setSelection(posLang)
//                    val keyLanguage = this.resources.getString(R.string.KEY_LANGUAGE)
//                    val nameSettingsPreferences = this.resources.getString(R.string.name_settings_preferences)
//                    val sharedPreferences= context?.getSharedPreferences(nameSettingsPreferences, Activity.MODE_PRIVATE)
//                    val language = sharedPreferences?.getString(keyLanguage, "")
//
//                    if (language != "") {
//                        var posSelected = 0
//                        val langCodes = activity!!.resources.getStringArray(R.array.languages_values)
//                        for ((index, langCode) in langCodes.withIndex()) {
//                            if (langCode == language) {
//                                posSelected = index
//                                break
//                            }
//                        }
//                        binding.spinnerLanguage.setSelection(posSelected)
//                    }
                }
            }
        )

    }

    private fun configureBtnSaveObserver() {
        viewModel.boolSave.observe(
            this as LifecycleOwner,
            Observer { shouldSave ->
                if (shouldSave) {
                    viewModel.saveConfigurations(this)
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
                val actionNavigate =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
