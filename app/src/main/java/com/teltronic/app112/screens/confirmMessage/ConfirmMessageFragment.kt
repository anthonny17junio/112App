package com.teltronic.app112.screens.confirmMessage


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentConfirmMessageBinding

/**
 * A simple [Fragment] subclass.
 */
class ConfirmMessageFragment : Fragment() {

    private lateinit var binding: FragmentConfirmMessageBinding
    private lateinit var viewModel: ConfirmMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirm_message,
            container,
            false
        )

        //Inicializo el viewModel
        val args = ConfirmMessageFragmentArgs.fromBundle(arguments!!)
        val subcategory = args.subcategory
        val viewModelFactory = ConfirmMessageViewModelFactory(subcategory)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ConfirmMessageViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.confirmMessageViewModel = viewModel
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
        inflater.inflate(R.menu.chats_right_menu, menu)
    }
}
