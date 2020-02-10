package com.teltronic.app112.screens.mainScreen


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentMainBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        Timber.i("Main fragment onCreateView")

        /*MODO 1*/
        /*
        binding.btnBorrar.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_locationFragment)
        }
        */
        /*MODO 2*/
        /*
        binding.btnBorrar.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_mainFragment_to_locationFragment)
        }
        */
        /*MODO 3*/
        /*binding.btnBorrar.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_locationFragment)
        )*/

        /*Módo 4*/
        binding.btnBorrar.setOnClickListener { view ->
            view.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToLocationFragment())
        }
        return binding.root
    }
}
