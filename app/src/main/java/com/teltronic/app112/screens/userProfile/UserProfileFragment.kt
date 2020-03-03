package com.teltronic.app112.screens.userProfile


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentUserProfileBinding

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_profile,
            container,
            false
        )

        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.userProfileViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        configureBackButton()
        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        return binding.root
    }

    private fun configureBackButton() {
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
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
                val actionNavigate = UserProfileFragmentDirections.actionUserProfileFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
