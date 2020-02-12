package com.teltronic.app112.screens.mainScreen


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        //Inicializo el viewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configNavigationsObservers()
        configCallButtonObserver()
        //"Uno" el layout con esta clase por medio del binding
        binding.mainViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        return binding.root
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

    private fun configNavigationsObservers() {
        configNavigateToNoticesObserver()
        configNavigateToLocationObserver()
        configNavigateToChatsObserver()
        configNavigateToNewChatObserver()
    }

    private fun configNavigateToNoticesObserver() {
        //Listener a navegación de avisos (notices)
        viewModel.boolNavigateToNotices.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToNoticesFragment()
                    findNavController().navigate(actionNavigate)
                    viewModel.onNavigateToNoticesComplete()
                }
            })
    }

    private fun configNavigateToLocationObserver() {
        //Listener a navegación de ubicación(location)
        viewModel.boolNavigateToLocation.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToLocationFragment()
                    findNavController().navigate(actionNavigate)
                    viewModel.onNavigateToLocationComplete()
                }
            })
    }

    private fun configNavigateToChatsObserver() {
        //Listener a navegación de chats
        viewModel.boolNavigateToChats.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate = MainFragmentDirections.actionMainFragmentToChatsFragment()
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateToChatsComplete()
                }
            })
    }

    private fun configNavigateToNewChatObserver() {
        //Listener a navegación de nuevo mensaje
        viewModel.boolNavigateToNewChat.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToNewChatFragment()
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateToNewChatComplete()
                }
            })
    }

}
