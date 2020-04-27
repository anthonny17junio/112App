package com.teltronic.app112.screens.chat


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.teltronic.app112.R
import com.teltronic.app112.adapters.MessagesAdapter
import com.teltronic.app112.databinding.FragmentChatBinding

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel
    val adapter = MessagesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )
        //Inicializo el viewModel
        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val idChat = args.idChat
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ChatViewModelFactory(application, idChat, this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.chatViewModel = viewModel

        binding.rvMessages.adapter = adapter
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha

        configureErrorSendMessageObserver()
        //Retorno el binding root (no el inflater)
        return binding.root
    }

    private fun configureErrorSendMessageObserver() {
        viewModel.strErrorSendMessage.observe(
            this as LifecycleOwner,
            Observer { strError ->
                if (strError != "") {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        strError,
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.clearStrErrorSendMessage()
                }
            }
        )
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
                val actionNavigate = ChatFragmentDirections.actionChatFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }

}
