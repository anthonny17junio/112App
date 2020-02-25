package com.teltronic.app112.screens.newChat


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.databinding.FragmentNewChatBinding
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.Subcategory


/**
 * A simple [Fragment] subclass.
 */
class NewChatFragment : Fragment() {

    private lateinit var binding: FragmentNewChatBinding
    private lateinit var viewModel: NewChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_chat,
            container,
            false
        )
        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(NewChatViewModel::class.java)

        configIconsObservers()
        configNavigationsObservers()
        configCallButtonObserver()

        //"Uno" el layout con esta clase por medio del binding
        binding.newChatViewModel = viewModel
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
        inflater.inflate(R.menu.new_chat_right_menu, menu)
    }

    private fun configNavigationsObservers() {
        configureNavigateToConfirmChatObserver()
    }


    @SuppressLint("DefaultLocale")
    private fun configIconsObservers() {
        viewModel.categoryNavigate.observe(
            this as LifecycleOwner,
            Observer { category ->
                if (category != null) {
                    val category = viewModel.categoryNavigate.value!!
                    val actionNavigate =
                        NewChatFragmentDirections.actionNewChatFragmentToSubcategoriesNewChatFragment(
                            category
                            , resources.getString(category.idTitle).toUpperCase()
                        )
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateCategoryComplete()
                }
            }
        )

        binding.btnCrime.setImageResource(viewModel.idCrimeIcon.value!!)
        binding.btnAccident.setImageResource(viewModel.idAccidentIcon.value!!)
        binding.btnMedicalUrgency.setImageResource(viewModel.idMedicalUrgencyIcon.value!!)
        binding.btnOther.setImageResource(viewModel.idOtherIcon.value!!)
    }

    private fun configureNavigateToConfirmChatObserver() {
        //Listener a navegación de avisos (notices)
        viewModel.boolNavigateToConfirmChat.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate =
                        NewChatFragmentDirections.actionNewChatFragmentToConfirmMessageFragment(
                            Subcategory.OTHER
                        )
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateToConfirmChatComplete()
                }
            })
    }

    private fun configCallButtonObserver() {
        viewModel.boolMakeCall.observe(
            this as LifecycleOwner,
            Observer { shouldCall ->
                if (shouldCall) {
                    viewModel.makeBoolCallComplete()
                    Phone.makeCall(activity!!)
                }
            }
        )
    }
}

