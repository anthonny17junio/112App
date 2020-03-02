package com.teltronic.app112.screens.chats


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.databinding.FragmentChatsBinding
import com.teltronic.app112.R


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
        inflater.inflate(R.menu.home_right_menu, menu)
    }

    //Navega al main fragment cuando se presiona el icono home
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeIconItem -> {
                val actionNavigate = ChatsFragmentDirections.actionChatsFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
