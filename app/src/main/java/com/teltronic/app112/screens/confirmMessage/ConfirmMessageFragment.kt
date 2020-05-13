package com.teltronic.app112.screens.confirmMessage


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.enums.PermissionsApp
import com.teltronic.app112.databinding.FragmentConfirmMessageBinding

/**
 * A simple [Fragment] subclass.
 */
class ConfirmMessageFragment : Fragment() {

    private lateinit var binding: FragmentConfirmMessageBinding
    private lateinit var viewModel: ConfirmMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirm_message,
            container,
            false
        )

        //Inicializo el viewModel
        val args = ConfirmMessageFragmentArgs.fromBundle(requireArguments())
        val subcategory = args.subcategory
        val act = requireNotNull(activity)
        val viewModelFactory = ConfirmMessageViewModelFactory(subcategory, act)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ConfirmMessageViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.confirmMessageViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        configureInfoRealtime()
        configureBackButton()
        configureConfirmButtonObserver()
        configureErrorCreateChatObserver()
        configureStartCreateChatObserver()
        //Retorno el binding root (no el inflater)
        return binding.root
    }

    private fun configureStartCreateChatObserver() {
        viewModel.createChat.observe(
            this as LifecycleOwner,
            Observer { shouldCreateChat ->
                if (shouldCreateChat) {
                    if ((!Phone.existPermission(
                            requireActivity().application,
                            PermissionsApp.WRITE_EXTERNAL_STORAGE
                        ) || !Phone.existPermission(
                            requireActivity().application,
                            PermissionsApp.READ_EXTERNAL_STORAGE
                        ))
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                PermissionsApp.WRITE_EXTERNAL_STORAGE.manifestName,
                                PermissionsApp.READ_EXTERNAL_STORAGE.manifestName
                            ),
                            PermissionsApp.WRITE_EXTERNAL_STORAGE.code
                        )
                    } else {
                        viewModel.startCreatingNewChat(binding.chkRealTimeLocation.isChecked)
                    }
                    viewModel.newChatCreated()
                }
            }
        )
    }

    private fun configureErrorCreateChatObserver() {
        viewModel.strErrorCreateChat.observe(
            this as LifecycleOwner,
            Observer { strError ->
                if (strError != "") {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        strError,
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.clearStrErrorCreateChat()
                }
            }
        )
    }

    private fun configureInfoRealtime() {
        binding.imgInfoRealTime.setOnClickListener {
            // Initialize a new instance of
            val builder = AlertDialog.Builder(activity)
            // Set the alert dialog title
            builder.setTitle(resources.getString(R.string.txt_chk_location))
            // Display a message on alert dialog
            builder.setMessage(resources.getString(R.string.txt_chk_location_info))
            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()
        }
    }

    private fun configureBackButton() {
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configureConfirmButtonObserver() {
        viewModel.idChatToNavigate.observe(
            this as LifecycleOwner,
            Observer { idChat ->
                if (idChat != null) {
                    val actionNavigate =
                        ConfirmMessageFragmentDirections.actionConfirmMessageFragmentToChatFragment(
                            idChat, viewModel.idUser
                        )
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateToChatComplete()
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
                val actionNavigate =
                    ConfirmMessageFragmentDirections.actionConfirmMessageFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
