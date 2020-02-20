package com.teltronic.app112.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
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
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Preferences.loadLocate(this)

        viewModelFactory = MainActivityViewModelFactory()
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.mainActivityViewModel = viewModel
        binding.lifecycleOwner = this
        configureLateralMenu()

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

        NavigationUI.setupWithNavController(binding.lateralMenu, navController)
        configureOnClickImageProfileLateralMenu(navController)
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

    //Configura el onClick de la foto de perfil del header del menú lateral
    private fun configureOnClickImageProfileLateralMenu(navController: NavController) {
        val imgProfileLateralMenu: ImageView =
            binding.lateralMenu.getHeaderView(0).findViewById(R.id.img_profile)
        imgProfileLateralMenu.setOnClickListener {
            val actionNavigate =
                MainFragmentDirections.actionMainFragmentToUserProfileFragment()
            navController.navigate(actionNavigate)
        }
    }



}

