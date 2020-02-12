package com.teltronic.app112.screens.medicalInfo


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
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
        viewModel = ViewModelProvider(this).get(MedicalInfoViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.medicalInfoViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)
        return binding.root
    }

    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.medical_info_screen_right_menu, menu)
    }
}
