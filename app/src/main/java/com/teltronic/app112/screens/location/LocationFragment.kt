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
        val binding: FragmentLocationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_location,
            container,
            false
        )
//        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java) //Antes
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java) //Ahora

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        return binding.root
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.location_screen_right_menu, menu)
    }
}
