package com.teltronic.app112.screens.location


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentLocationBinding

/**
 * A simple [Fragment] subclass.
 */
class LocationFragment : Fragment() {

    private lateinit var viewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        val binding: FragmentLocationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_location,
            container,
            false
        )
        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java) //Ahora

        //"Uno" el layout con esta clase por medio del binding
        binding.locationViewModel = viewModel
        //Para que el ciclo de vida del binding sea sonsistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        return binding.root
    }

    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.location_screen_right_menu, menu)
    }
}
