package com.teltronic.app112.screens.mainScreen


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.PermissionsApp
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {


//    private val executor = Executor { }

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
                    if (!Phone.isLocationEnabled(activity)) {
                        Toast.makeText(
                            activity,
                            R.string.location_no_enabled,
                            Toast.LENGTH_LONG
                        ).show()
                    } else
                        if (Phone.existPermission(
                                activity,
                                PermissionsApp.FINE_LOCATION
                            )
                        ) {
                            //Si tienes permisos de localización navegas a location screen
                            val actionNavigate =
                                MainFragmentDirections.actionMainFragmentToLocationFragment()
                            findNavController().navigate(actionNavigate)
                        } else {
                            //Si no tienes permisos de localización los pides
                            Phone.askPermission(activity!!,
                                PermissionsApp.FINE_LOCATION)
                        }
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
                    findNavController().navigate(actionNavigate) //Si se desea ir directamente
//                    Phone.biometricAuth(activity!!, findNavController(), actionNavigate) //Si se desea que pida la huella para ir
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
