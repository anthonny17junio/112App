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

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.mainViewModel = viewModel //Se "une" el layout con esta clase
        binding.lifecycleOwner = this //Permite que funcione android:text="@{gameViewModel.word}"  --> Para que su ciclo de vida de enlace de datos sea consciente y para que funcione bien con LiveData, debe llamar a binding.setLifecycleOwner

        /*Modo 5*/
        //Configura la navegación
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