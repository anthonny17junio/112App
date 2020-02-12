package com.teltronic.app112.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.teltronic.app112.R
import com.teltronic.app112.classes.Phone
import com.teltronic.app112.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicializo el binding
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        configureLateralMenu()
    }

    //Menú lateral
    private fun configureLateralMenu() {
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //Para evitar que se abra el menú en otras pantallas que no sea la principal
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == controller.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupWithNavController(binding.lateralMenu, navController)
    }

    //Habilita el back button
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    //Al presionar back button cierra el menú (solo si está abierto)
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //Al presionar otra vez el icono del menú se cierra (solo si está abierto)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Phone.makeActionRequestPermissionsResult(this,requestCode,grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}

