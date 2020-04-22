package com.teltronic.app112.screens.location


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.databinding.FragmentLocationBinding

/**
 * A simple [Fragment] subclass.
 */
class LocationFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentLocationBinding
    private lateinit var viewModel: LocationViewModel

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_location,
            container,
            false
        )
        //Inicializo el viewModel
        val viewModelFactory = LocationViewModelFactory(activity)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(LocationViewModel::class.java) //Ahora

        coordinatesObserver()

        //"Uno" el layout con esta clase por medio del binding
        binding.locationViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        btnCopyObserver()
        configNavigateToNewChatObserver()
        configCallButtonObserver()

        return binding.root
    }

    override fun onMapReady(gMap: GoogleMap?) { //Se ejecuta cada vez que el mapa está preparado
        MapsInitializer.initialize(context)
        if (gMap != null) {
            mGoogleMap = gMap
            gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            val location = viewModel.coordinates.value
            if (::mGoogleMap.isInitialized && location != null) {
                moveCamera(location, mGoogleMap)
            }
        }
    }

    private fun coordinatesObserver() { //Observa el cambio de coordenadas
        viewModel.coordinates.observe(this as LifecycleOwner, Observer { newCoordinates ->
            if (::mGoogleMap.isInitialized)
                moveCamera(newCoordinates, mGoogleMap)
        })
    }

    private fun btnCopyObserver() {
        viewModel.boolCopy.observe(this as LifecycleOwner, Observer { shouldCopy ->
            if (shouldCopy) {
                viewModel.copyLatLang()
                Toast.makeText(activity, R.string.lat_long_copied, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun moveCamera(latLang: LatLng, gMap: GoogleMap?) {
        gMap?.addMarker(MarkerOptions().position(latLang))

        val cameraPosition =
            CameraPosition.builder().target(latLang).zoom(17f).bearing(0f).tilt(45f).build()
        gMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun configNavigateToNewChatObserver() {
        //Listener a navegación de nuevo mensaje
        viewModel.boolNavigateToNewChat.observe(
            this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val actionNavigate =
                        LocationFragmentDirections.actionLocationFragmentToNewChatFragment()
                    findNavController().navigate(actionNavigate)
                    viewModel.navigateToNewChatComplete()
                }
            })
    }

    private fun configCallButtonObserver() {
        viewModel.boolMakeCall.observe(
            this as LifecycleOwner,
            Observer { shouldCall ->
                if (shouldCall) {
                    viewModel.makeBoolCallComplete()
                    Phone.makeCall(requireActivity())
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Se inicializa el mapa
        mMapView = binding.map
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)
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
                val actionNavigate = LocationFragmentDirections.actionLocationFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }
}
