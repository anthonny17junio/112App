package com.teltronic.app112.screens.chats

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.databinding.FragmentChatsBinding
import com.teltronic.app112.R
import com.teltronic.app112.adapters.ChatListener
import com.teltronic.app112.adapters.ChatsAdapter
import com.teltronic.app112.screens.MainActivity


/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var viewModel: ChatsViewModel
    private lateinit var adapter: ChatsAdapter

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
        val viewModelFactory = ChatsViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChatsViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.chatsViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        adapter = ChatsAdapter(ChatListener { chatId ->
            viewModel.onChatClicked(chatId)
        })
        binding.rvChats.adapter = adapter

        configureErrorObserver()
        configChatsObserver()
        configOnclickChatObserver()

        return binding.root
    }

    override fun onDestroy() {
        (activity as MainActivity).hideSnackbar()
        super.onDestroy()
    }

    override fun onDestroyView() {
        (activity as MainActivity).hideSnackbar()
        super.onDestroyView()
    }

    private fun configOnclickChatObserver() {
        viewModel.navigateToChat.observe(this as LifecycleOwner, Observer { idChat ->
            idChat?.let {
                this.findNavController().navigate(
                    ChatsFragmentDirections.actionChatsFragmentToChatFragment(idChat, viewModel.idUser)
                )
                viewModel.onChatNavigated()
            }
        })
    }

    private fun configChatsObserver() {
        viewModel.chats.observe(
            this as LifecycleOwner,
            Observer { chats ->
                chats?.let {
                    adapter.submitList(chats)
                }
            }
        )
    }

    private fun configureErrorObserver() {
        viewModel.strError.observe(
            this as LifecycleOwner,
            Observer { strError ->
                if (strError != "") {
                    (activity as MainActivity).showSnackbar(strError)
                } else {
                    (activity as MainActivity).hideSnackbar()
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
                val actionNavigate = ChatsFragmentDirections.actionChatsFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
