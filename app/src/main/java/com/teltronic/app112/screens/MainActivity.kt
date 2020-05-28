@file:Suppress("DEPRECATION")

package com.teltronic.app112.screens

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.teltronic.app112.databinding.ActivityMainBinding
import com.teltronic.app112.screens.mainScreen.MainFragmentDirections
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.snackbar.Snackbar
import com.teltronic.app112.R
import com.teltronic.app112.classes.DownloadImageTask
import com.teltronic.app112.classes.GoogleApiPeopleHelper
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.classes.Preferences
import com.teltronic.app112.classes.enums.IntCodes
import com.teltronic.app112.database.room.DatabaseApp
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.lang.ref.WeakReference

@Suppress("SENSELESS_COMPARISON")
class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        createNotificationChannel()

        Preferences.loadLocate(this)

        val viewModelFactory = MainActivityViewModelFactory(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.mainActivityViewModel = viewModel
        binding.lifecycleOwner = this

        configureLateralMenu()
        configureNavigationObservers()

        trySendNotification()
    }

    private fun trySendNotification() {
        val viewModelJob = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        uiScope.launch {
            trySendNotificationIO()
        }
    }

    private suspend fun trySendNotificationIO() {
        withContext(Dispatchers.IO) {
            val configurations = DatabaseApp.getInstance(this@MainActivity).configurationsDao
            if (configurations != null) {
                val arguments = Bundle()
                val idChat = "52748f41-fc1a-437f-8ad9-ceb99c5c178d"
                arguments.putString("idChat", idChat)
                arguments.putString("idUserRoom", configurations.get().id_rethink)

                val pendingIntent: PendingIntent = NavDeepLinkBuilder(this@MainActivity)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.navigation)
                    .setDestination(R.id.chatFragment)
                    .setArguments(arguments)
                    .createPendingIntent()

                val builder = NotificationCompat.Builder(this@MainActivity, "icChannel")
                    .setSmallIcon(R.drawable.ic_notification_logo)
                    .setContentTitle(getString(R.string.notification_new_message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
//
                with(NotificationManagerCompat.from(this@MainActivity)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(0, builder.build())
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal xyz"
            val descriptionText = "Descripción del canal"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("icChannel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun hideSnackbar() {
        if (::snackbar.isInitialized)
            snackbar.dismiss()
    }

    fun showSnackbar(message: String) {
        snackbar =
            Snackbar.make(
                this.findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_INDEFINITE
            )
        snackbar.show()
    }

    //Menú lateral
    private fun configureLateralMenu() {
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //Para evitar que se abra el menú en otras pantallas que no sea la principal
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        //NavigationUI.setupWithNavController(binding.lateralMenu, navController) //(Con esta línea se evita programar los navigation de cada uno de los botones (configureOnItemSelectedLateralMenu), basta con poner el id del fragment en el id del item en main_lateral_menu.xml, sin embargo deseo hacer una verificación biométrica antes de navegar)
        configureOnClickImageProfileLateralMenu()
        configureOnItemSelectedLateralMenu()
    }

    //Configura el onClick de la foto de perfil o el nombre de usuario del header del menú lateral
    private fun configureOnClickImageProfileLateralMenu() {
        val imgProfileLateralMenu: ImageView =
            binding.lateralMenu.getHeaderView(0).findViewById(R.id.img_profile)
        val txtNameProfileLateralMenu: TextView =
            binding.lateralMenu.getHeaderView(0).findViewById(R.id.txtName)

        imgProfileLateralMenu.setOnClickListener {
            viewModel.tryNavigateToUserProfile()
        }
        txtNameProfileLateralMenu.setOnClickListener {
            viewModel.tryNavigateToUserProfile()
        }
    }

    //Habilita el back button
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    //Al presionar back button cierra el menú (solo si está abierto)
    override fun onBackPressed() {
        val drawer = this.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //Al presionar otra vez el icono del menú se cierra (solo si está abierto)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawer = this.drawerLayout
        return if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    //Al conceder o no persmisos se ejecuta esta función
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Phone.makeActionRequestPermissionsResult(this, requestCode, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun configureOnItemSelectedLateralMenu() {
        val menu = binding.lateralMenu.menu

        configUserProfileItemClickListener(menu)
        configMedicalInfoItemClickListener(menu)
        configConfigurationItemClickListener(menu)
        configLegalNoticeItemClickListener(menu)
        configAboutItemClickListener(menu)
    }

    //Navigation observers
    private fun configureNavigationObservers() {
        configureTryNavigationUserProfileObserver() //Cuando doy click (inicia autenticación)
        configureBiometricAuthToUserProfileObserver() //Cuando me autentico biométricamente
        configureNavigationUserProfileObserver() //Cuando voy a la pantalla

        configureTryNavigationMedicalInfoObserver() //Cuando doy click
        configureNavigationMedicalInfoObserver() //Cuando voy a la pantalla

        configureNavigationConfigurationObserver()
        configureNavigationLegalNoticeObserver()
        configureNavigationAboutObserver()

        configureImageProfileObserver()
        configureGoogleAccountObserver() //Cuando me autentico con la cuenta de google
    }


    //User profile click listener and observer
    //**************************************************
    private fun configUserProfileItemClickListener(menu: Menu) {
        menu.findItem(R.id.userProfileFragment).setOnMenuItemClickListener {
            viewModel.tryNavigateToUserProfile()
            true
        }
    }

    private fun configureTryNavigationUserProfileObserver() {
        viewModel.boolTryNavigateToUserProfile.observe(this as LifecycleOwner,
            Observer { tryNavigate ->
                if (tryNavigate) {
                    try {
                        Phone.tryBiometricAuth(
                            this,
                            viewModel.getLiveDataBiometricAuthToUserProfile()
                        )
                    } catch (e: Exception) {
                        Timber.e("${this.javaClass.name} - ${e.message}")
                    }
                    viewModel.navigateToUserProfileTried()
                }
            }
        )
    }

    private fun configureBiometricAuthToUserProfileObserver() {
        viewModel.boolBiometricAuthToUserProfile.observe(this as LifecycleOwner,
            Observer { isBiometricAuthenticated ->
                if (isBiometricAuthenticated) {
                    if (viewModel.boolGoogleAuthenticated.value!!) { //Si está autenticado con google ir a la user profile
                        viewModel.navigateToUserProfile()
                    } else { //Si no está autenticado pedir sesión google
                        GoogleApiPeopleHelper.googleAuth(
                            IntCodes.CODE_REQUEST_GOOGLE_AUTH_EDIT_PROFILE.code,
                            this
                        )
                        viewModel.googleAuthAsked()
                    }
                    viewModel.resetBiometricUserProfileAuth()
                }
            }
        )
    }

    private fun configureNavigationUserProfileObserver() {
        viewModel.boolNavigateToUserProfile.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val navCont = findNavController(R.id.myNavHostFragment)
                    if (navCont.currentDestination?.id == navCont.graph.startDestination) {
                        val actionNavigate =
                            MainFragmentDirections.actionMainFragmentToUserProfileFragment()
                        navCont.navigate(actionNavigate)
                    }
                    viewModel.navigateToUserProfileComplete()
                }
            }
        )
    }

    //Medical information click listener and observer
    //**************************************************
    private fun configMedicalInfoItemClickListener(menu: Menu) {
        menu.findItem(R.id.medicalInfoFragment).setOnMenuItemClickListener {
            viewModel.tryNavigateToMedicalInfo()
            true
        }
    }

    private fun configureTryNavigationMedicalInfoObserver() {
        viewModel.boolTryNavigateToMedicalInfo.observe(this as LifecycleOwner,
            Observer { tryNavigate ->
                if (tryNavigate) {
                    Phone.tryBiometricAuth(this, viewModel.getLiveDataNavigateToMedicalInfo())
                    viewModel.navigateToMedicalInfoTried()
                }
            }
        )
    }

    private fun configureNavigationMedicalInfoObserver() {
        viewModel.boolNavigateToMedicalInfo.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val navCont = findNavController(R.id.myNavHostFragment)
                    if (navCont.currentDestination?.id == navCont.graph.startDestination) {
                        val actionNavigate =
                            MainFragmentDirections.actionMainFragmentToMedicalInfoFragment()
                        navCont.navigate(actionNavigate)
                    }
                    viewModel.navigateToMedicalInfoComplete()
                }
            }
        )
    }

    //Configuration click listener and observer
    // **************************************************
    private fun configConfigurationItemClickListener(menu: Menu) {
        menu.findItem(R.id.configurationFragment).setOnMenuItemClickListener {
            viewModel.navigateToConfiguration()
            true
        }
    }

    private fun configureNavigationConfigurationObserver() {
        viewModel.boolNavigateToConfiguration.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val navCont = findNavController(R.id.myNavHostFragment)
                    if (navCont.currentDestination?.id == navCont.graph.startDestination) {
                        val actionNavigate =
                            MainFragmentDirections.actionMainFragmentToConfigurationFragment()
                        navCont.navigate(actionNavigate)
                    }
                    viewModel.navigateToConfigurationComplete()
                }
            }
        )
    }

    //Legal notice click listener and observer
    // **************************************************
    private fun configLegalNoticeItemClickListener(menu: Menu) {
        menu.findItem(R.id.legalNoticeFragment).setOnMenuItemClickListener {
            viewModel.navigateToLegalNotice()
            true
        }
    }

    private fun configureNavigationLegalNoticeObserver() {
        viewModel.boolNavigateToLegalNotice.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val navCont = findNavController(R.id.myNavHostFragment)
                    if (navCont.currentDestination?.id == navCont.graph.startDestination) {
                        val actionNavigate =
                            MainFragmentDirections.actionMainFragmentToLegalNoticeFragment()
                        navCont.navigate(actionNavigate)
                    }
                    viewModel.navigateToLegalNoticeComplete()
                }
            }
        )
    }

    //About click listener and observer
    // **************************************************
    private fun configAboutItemClickListener(menu: Menu) {
        menu.findItem(R.id.aboutFragment).setOnMenuItemClickListener {
            viewModel.navigateToAbout()
            true
        }
    }

    private fun configureNavigationAboutObserver() {
        viewModel.boolNavigateToAbout.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
                    val navCont = findNavController(R.id.myNavHostFragment)
                    if (navCont.currentDestination?.id == navCont.graph.startDestination
                    ) {
                        val actionNavigate =
                            MainFragmentDirections.actionMainFragmentToAboutFragment()
                        navCont.navigate(actionNavigate)
                    }
                    viewModel.navigateToAboutComplete()
                }
            }
        )
    }

    //**********************************************************************************************
    //GOOGLE ACCOUNT
    //**********************************************************************************************
    //Google account observer (observer de la variable que indica si está autenticado o no)
    private fun configureGoogleAccountObserver() {
        viewModel.boolGoogleAuthenticated.observe(this as LifecycleOwner,
            Observer { isAuthenticatedWithGoogle ->
                if (isAuthenticatedWithGoogle) { //Si está autenticado con google setea la
                    this.displayGoogleAccountInfo()
                } else if (viewModel.shouldAskGoogleAuth.value!!) { //Verifica si ha pedido antes iniciar sesión o no
                    GoogleApiPeopleHelper.googleAuth(
                        IntCodes.CODE_REQUEST_GOOGLE_AUTH_MAIN.code,
                        this
                    )
                    viewModel.googleAuthAsked()
                }
            })
    }

    //Setea el nombre de la cuenta de google en el menú lateral y descarga la foto de perfil
    private fun displayGoogleAccountInfo() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val header = binding.lateralMenu.getHeaderView(0)
        val txtName: TextView = header.findViewById(R.id.txtName)

        txtName.text = account!!.displayName
        //Debe ser weak reference porque puede pasar que al cargar la imagen el parámetro ya no exista
        DownloadImageTask(
            resources,
            WeakReference(viewModel.getLiveDataProfileImageBitmap())
        ).execute(account.photoUrl.toString())
    }

    //Si existe una imagen de la cuenta de google la setea en el menú lateral
    private fun configureImageProfileObserver() {
        viewModel.profileImage.observe(this as LifecycleOwner,
            Observer { imageBitmap ->
                val imgProfileLateralMenu: ImageView =
                    binding.lateralMenu.getHeaderView(0).findViewById(R.id.img_profile)
                imgProfileLateralMenu.setImageBitmap(imageBitmap)
            }
        )
    }

    //Después de iniciar la sesión de google se ejecuta esto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Si se ha iniciado sesión seteo en true la variable del view model
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IntCodes.CODE_REQUEST_GOOGLE_AUTH_EDIT_PROFILE.code -> { //Al loguearse después de dar click en el perfil de usuario
                    viewModel.navigateToUserProfile() //Ir a la pantalla de user profile
                }

                //Descomentar si se quiere que pase algo después de loguearse por primera vez (por ejemplo guardar los datos de la cuenta de google en alguna base de datos)
//                IntCodes.CODE_REQUEST_GOOGLE_AUTH_MAIN.code -> { //Al loguearse por primera vez (sin dar click a nada previamente)
//                }
//                IntCodes.CODE_REQUEST_GOOGLE_AUTH_FRAGMENT_PROFILE.code -> { //Al loguearse por primera vez (sin dar click a nada previamente)

            }

            //Si está autenticado obtener el id de rethinkDB
            if (requestCode == IntCodes.CODE_REQUEST_GOOGLE_AUTH_EDIT_PROFILE.code || requestCode == IntCodes.CODE_REQUEST_GOOGLE_AUTH_MAIN.code) {
                viewModel.syncDatabasesAfterGoogleAuth()
            }
            viewModel.authenticationWithGoogleComplete()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //Si la conexión para la autenticación con google falla se muestra un dialog
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        val mGoogleApiAvailability = GoogleApiAvailability.getInstance()
        val dialog =
            mGoogleApiAvailability.getErrorDialog(
                this,
                connectionResult.errorCode, 1000
            )
        dialog.show()
    }
    // **********************************************************************************************
    //GOOGLE ACCOUNT
    //**********************************************************************************************
}
