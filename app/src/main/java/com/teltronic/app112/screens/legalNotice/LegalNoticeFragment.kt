package com.teltronic.app112.screens.legalNotice


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentLegalNoticeBinding

/**
 * A simple [Fragment] subclass.
 */
class LegalNoticeFragment : Fragment() {

    private lateinit var binding: FragmentLegalNoticeBinding
    private lateinit var viewModel: LegalNoticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_legal_notice,
            container,
            false
        )

        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(LegalNoticeViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.legalNoticeViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this
        //Retorno el binding root (no el inflater)

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        return binding.root
    }

    //Inicia el menú de la derecha (en este caso solo es un icono)
    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) { //Habilita el icono de la derecha
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.legal_notice_screen_right_menu, menu)
    }
}
