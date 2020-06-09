package com.teltronic.app112.screens.configuration


import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.DistanceValues
import com.teltronic.app112.databinding.FragmentConfigurationBinding

/**
 * A simple [Fragment] subclass.
 */
class ConfigurationFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentConfigurationBinding
    private lateinit var viewModel: ConfigurationViewModel

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var circle: Circle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inicializo el binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_configuration,
            container,
            false
        )
        //Inicializo el viewModel
        val viewModelFactory = ConfigurationViewModelFactory(activity)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ConfigurationViewModel::class.java)
        viewModel.loadConfigurations(this.requireActivity())

        coordinatesObserver()

        //"Uno" el layout con esta clase por medio del binding
        binding.configurationViewModel = viewModel
        //Para que el ciclo de vida del binding sea consistente y funcione bien con LiveData
        binding.lifecycleOwner = this

        setHasOptionsMenu(true) //Habilita el icono de la derecha
        //Retorno el binding root (no el inflater)

        configureBtnSaveObserver()
        configureBackButton()
        configurePosSelectedLanguageObserver()
        configureDistanceBarChange()
        configureDistanceIdObserver()
        return binding.root
    }

    private fun configurePosSelectedLanguageObserver() {
        viewModel.posSelectedLanguage.observe(
            this as LifecycleOwner,
            Observer { posLang ->
                if (posLang != -1) {
                    binding.spinnerLanguage.setSelection(posLang)
                }
            }
        )
    }

    private fun configureDistanceIdObserver() {
        viewModel.currentDistanceId.observe(
            this as LifecycleOwner,
            Observer { distanceId ->
                when (DistanceValues.getById(distanceId)) {
                    DistanceValues.NONE_KM ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_no_recieve_notices)
                    DistanceValues.NO_LIMIT ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_no_limits_recieve_notices)
                    else ->
                        viewModel._strMessageDistance.value =
                            getString(R.string.txt_recieve_notices) + " " +
                                    DistanceValues.getById(distanceId)!!.valueKm.toString() + " km"
                }
            }
        )
    }

    private fun configureDistanceBarChange() {
        binding.seekBarDistance.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel._currentDistanceId.value = progress
                val location = viewModel.coordinates.value
                val radius: Double
                if (location != null) {
                    when (DistanceValues.getById(progress)) {
                        DistanceValues.NONE_KM -> {
                            radius = 0.0
                        }
                        DistanceValues.NO_LIMIT -> {
                            moveCamera(location, mGoogleMap, 4f)
                            radius = 0.0
                        }
                        DistanceValues.ONE_KM -> {
                            moveCamera(location, mGoogleMap, 13f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        DistanceValues.FIVE_KM -> {
                            moveCamera(location, mGoogleMap, 11f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        DistanceValues.TEN_KM -> {
                            moveCamera(location, mGoogleMap, 10f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        DistanceValues.THIRTY_KM -> {
                            moveCamera(location, mGoogleMap, 8f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        DistanceValues.FIFTY_KM -> {
                            moveCamera(location, mGoogleMap, 7.5f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        DistanceValues.ONE_HUNDRED_KM -> {
                            moveCamera(location, mGoogleMap, 7f)
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                        else -> {
                            radius = DistanceValues.getById(progress)!!.valueKm.toDouble()
                        }
                    }
                    if (::circle.isInitialized)
                        circle.remove()
                    drawCircleOnMap(radius)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun drawCircleOnMap(radius: Double) {
        val location = viewModel.coordinates.value
        circle = mGoogleMap.addCircle(
            CircleOptions()
                .center(location)
                .radius(radius * 1000)
                .strokeColor(Color.TRANSPARENT)
                .fillColor(0x220000FF)
                .strokeWidth(2F)
        )
    }

    private fun configureBtnSaveObserver() {
        viewModel.boolSave.observe(
            this as LifecycleOwner,
            Observer { shouldSave ->
                if (shouldSave) {
                    viewModel.saveConfigurations(this)
                }
            }
        )
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
                val actionNavigate =
                    ConfigurationFragmentDirections.actionConfigurationFragmentToMainFragment()
                findNavController().navigate(actionNavigate)
                true
            }
            else ->
                false
        }
    }

    //**********************************************************************************************
    //GOOGLE MAP
    //**********************************************************************************************
    override fun onMapReady(gMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        if (gMap != null) {
            mGoogleMap = gMap
            gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            val location = viewModel.coordinates.value
            if (::mGoogleMap.isInitialized && location != null) {
                moveCamera(location, mGoogleMap, 17f)
            }
        }
    }

    private fun coordinatesObserver() { //Observa el cambio de coordenadas
        viewModel.coordinates.observe(this as LifecycleOwner, Observer { newCoordinates ->
            if (::mGoogleMap.isInitialized)
                moveCamera(newCoordinates, mGoogleMap, 17f)
        })
    }

    private fun moveCamera(latLang: LatLng, gMap: GoogleMap?, zoom: Float) {
        gMap?.addMarker(MarkerOptions().position(latLang))

        val cameraPosition =
            CameraPosition.builder().target(latLang).zoom(zoom).bearing(0f).tilt(45f).build()
        gMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Se inicializa el mapa
        mMapView = binding.map
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)
    }

    //**********************************************************************************************
    //FIN GOOGLE MAP
    //**********************************************************************************************
}
