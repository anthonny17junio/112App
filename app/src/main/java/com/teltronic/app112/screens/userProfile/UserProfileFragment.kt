package com.teltronic.app112.screens.userProfile


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth

import com.teltronic.app112.R
import com.teltronic.app112.classes.GoogleApiPeopleHelper
import com.teltronic.app112.classes.IntCodes
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
        val viewModelFactory = UserProfileViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserProfileViewModel::class.java)

        //"Uno" el layout con esta clase por medio del binding
        binding.userProfileViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        configureImageProfile()
        setHasOptionsMenu(true) //Habilita el icono de la derecha
        configureAditionalDataUserObservers() //Configura los observers de los datos adicionales que trae People Api (cumpleaños y género)

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IntCodes.CODE_REQUEST_GOOGLE_AUTH_FRAGMENT_PROFILE.code -> { //Al loguearse después de dar click en el perfil de usuario
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    if (result.isSuccess) {
                        val account = result.signInAccount
                        val authCode = account?.serverAuthCode
                        GoogleApiPeopleHelper.PeopleAsyncTask(viewModel, activity!!.resources)
                            .execute(authCode)
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun configureImageProfile() {
        binding.imgProfile.setImageBitmap(viewModel.profileImage.value)
        configureImageProfileObserver()
    }

    private fun configureImageProfileObserver() {
        viewModel.profileImage.observe(this as LifecycleOwner,
            Observer { imageBitmap ->
                binding.imgProfile.setImageBitmap(imageBitmap)
            }
        )
    }

    private fun configureAditionalDataUserObservers() {
        configureGenderObserver()
        configureBirtdayObserver()
    }

    private fun configureGenderObserver() {
        viewModel.strGender.observe(this as LifecycleOwner,
            Observer { strGender ->
                if (strGender == "") {
                    binding.lvGender.visibility = View.GONE
                    binding.lvGenderTitle.visibility = View.GONE
                } else {
                    binding.lvGender.visibility = View.VISIBLE
                    binding.lvGenderTitle.visibility = View.VISIBLE
                }
            })
    }

    private fun configureBirtdayObserver() {
        viewModel.strBirthDay.observe(this as LifecycleOwner,
            Observer { strBirthDay ->
                if (strBirthDay == "") {
                    binding.lvBirthday.visibility = View.GONE
                    binding.lvBirthdayTitle.visibility = View.GONE
                } else {
                    binding.lvBirthday.visibility = View.VISIBLE
                    binding.lvBirthdayTitle.visibility = View.VISIBLE
                }
            })
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
                    UserProfileFragmentDirections.actionUserProfileFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
