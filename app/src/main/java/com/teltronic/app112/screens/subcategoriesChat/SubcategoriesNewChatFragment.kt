package com.teltronic.app112.screens.subcategoriesChat


import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
import com.teltronic.app112.adapters.SubcategoriesListAdapter
import com.teltronic.app112.databinding.FragmentSubcategoriesNewChatBinding

/**
 * A simple [Fragment] subclass.
 */
class SubcategoriesNewChatFragment : Fragment() {

    private lateinit var binding: FragmentSubcategoriesNewChatBinding
    private lateinit var viewModel: SubcategoriesNewChatViewModel
    private lateinit var viewModelFactory: SubcategoriesNewChatViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_subcategories_new_chat,
            container,
            false
        )
        //Inicializo el viewModel
        val args = SubcategoriesNewChatFragmentArgs.fromBundle(arguments!!)
        val category = args.category

        viewModelFactory = SubcategoriesNewChatViewModelFactory(category, activity as Activity)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(SubcategoriesNewChatViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.subcategoriesViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        configSubcategoriesObserver()
        configListItemListener()

        return binding.root
    }

    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.new_chat_right_menu, menu)
    }

    private fun configSubcategoriesObserver() {
        viewModel.listAdapter.observe(
            this as LifecycleOwner,
            Observer { listAdapter ->
                binding.lvSubcategories.adapter = listAdapter
            }
        )
    }

    private fun configListItemListener(){
        binding.lvSubcategories.setOnItemClickListener{parent, view, position, id ->

        }
    }
}
