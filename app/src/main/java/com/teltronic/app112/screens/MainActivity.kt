package com.teltronic.app112.screens

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
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.databinding.ActivityMainBinding
import androidx.navigation.NavController
import com.teltronic.app112.R
import com.teltronic.app112.classes.Preferences
import com.teltronic.app112.screens.mainScreen.MainFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Preferences.loadLocate(this)

        val viewModelFactory = MainActivityViewModelFactory()
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

    //Configura el onClick de la foto de perfil del header del menú lateral
    private fun configureOnClickImageProfileLateralMenu() {
        val imgProfileLateralMenu: ImageView = binding.lateralMenu.getHeaderView(0).findViewById(R.id.img_profile)
        imgProfileLateralMenu.setOnClickListener {
            viewModel.navigateToUserProfile(this)
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
        configureNavigationUserProfileObserver()
        configureNavigationMedicalInfoObserver()
        configureNavigationConfigurationObserver()
        configureNavigationLegalNoticeObserver()
        configureNavigationAboutObserver()
    }


    //User profile click listener and observer
    //**************************************************
    private fun configUserProfileItemClickListener(menu: Menu) {
        menu.findItem(R.id.userProfileFragment).setOnMenuItemClickListener {
            viewModel.navigateToUserProfile(this)
            false
        }
    }

    private fun configureNavigationUserProfileObserver() {
        viewModel.boolNavigateToUserProfile.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
//                    if (navController.currentDestination?.id == navController.graph.startDestination) {
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToUserProfileFragment()
                    findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
//                    }
                    viewModel.navigateToUserProfileComplete()
                }
            }
        )
    }

    //Medical information click listener and observer
    //**************************************************
    private fun configMedicalInfoItemClickListener(menu: Menu) {
        menu.findItem(R.id.medicalInfoFragment).setOnMenuItemClickListener {
            viewModel.navigateToMedicalInfo(this)
            true
        }
    }

    private fun configureNavigationMedicalInfoObserver() {
        viewModel.boolNavigateToMedicalInfo.observe(this as LifecycleOwner,
            Observer { shouldNavigate ->
                if (shouldNavigate) {
//                    if (navController.currentDestination?.id == navController.graph.startDestination) {
                    val actionNavigate =
                        MainFragmentDirections.actionMainFragmentToMedicalInfoFragment()
                    findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
//                    }
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
 //                    if (navController.currentDestination?.id == navController.graph.startDestination) {
                     val actionNavigate =
                         MainFragmentDirections.actionMainFragmentToConfigurationFragment()
                     findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
 //                    }
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
 //                    if (navController.currentDestination?.id == navController.graph.startDestination) {
                     val actionNavigate =
                         MainFragmentDirections.actionMainFragmentToLegalNoticeFragment()
                     findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
 //                    }
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
 //                    if (navController.currentDestination?.id == navController.graph.startDestination) {
                     val actionNavigate =
                         MainFragmentDirections.actionMainFragmentToAboutFragment()
                     findNavController(R.id.myNavHostFragment).navigate(actionNavigate)
 //                    }
                     viewModel.navigateToAboutComplete()
                 }
             }
         )
     }

}
