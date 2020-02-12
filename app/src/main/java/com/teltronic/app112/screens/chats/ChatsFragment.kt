package com.teltronic.app112.screens.chats


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentChatsBinding

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewModel: ChatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chats,
            container,
            false
        )
        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(ChatsViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.chatsViewModel = viewModel
        //Para que el ciclo de vida del binding sea sonsistente y funcione bien con LiveData
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
