package com.teltronic.app112.screens.mainScreen


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentMainBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.mainViewModel = viewModel
        //Para que el ciclo de vida del binding sea sonsistente y funcione bien con LiveData
        binding.lifecycleOwner = this


        //Navegación de ubicación
        viewModel.boolNavigateToLocation.observe(this as LifecycleOwner, Observer { goToLocation ->
            if (goToLocation) {
                val action = MainFragmentDirections.actionMainFragmentToLocationFragment()
                findNavController().navigate(action)
                viewModel.onNavigateToLocationComplete()
            }
        })
        return binding.root
    }

}


/*MODO 1*/
/*
binding.btnBorrar.setOnClickListener { view ->
    Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_locationFragment)
}
*/
//MODO 2
/*
binding.btnBorrar.setOnClickListener { view ->
    view.findNavController().navigate(R.id.action_mainFragment_to_locationFragment)
}
*/
//MODO 3
/*binding.btnBorrar.setOnClickListener(
    Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_locationFragment)
)*/
//Módo 4
/*binding.btnLocation.setOnClickListener { view ->
    view.findNavController()
        .navigate(MainFragmentDirections.actionMainFragmentToLocationFragment())
}*/