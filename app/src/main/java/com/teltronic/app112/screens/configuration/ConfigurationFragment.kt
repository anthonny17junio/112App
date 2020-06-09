package com.teltronic.app112.screens.configuration


import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.DistanceValues
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
        configureDistanceBarChange()
        configureDistanceIdObserver()
        return binding.root
    }

    private fun configurePosSelectedLanguageObserver() {
        viewModel.posSelectedLanguage.observe(
            this as LifecycleOwner,
            Observer { posLang ->
                if (posLang != -1) {
                    binding.spinnerLanguage.setSelection(posLang)
                }
            }
        )
    }

    private fun configureDistanceIdObserver() {
        viewModel.currentDistanceId.observe(
            this as LifecycleOwner,
            Observer { distanceId ->
                when (DistanceValues.getById(distanceId)) {
                    DistanceValues.NONE_KM ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_no_recieve_notices)
                    DistanceValues.NO_LIMIT ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_no_limits_recieve_notices)
                    else ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_recieve_notices) + " " +
                                    DistanceValues.getById(distanceId)!!.valueKm.toString() + " km"
                }
            }
        )
    }

    private fun configureDistanceBarChange() {
        binding.seekBarDistance.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel._currentDistanceId.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
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
