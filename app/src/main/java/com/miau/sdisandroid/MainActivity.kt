package com.miau.sdisandroid

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Layout
import com.miau.sdisandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //TODO meter esto en funciones y cambiarlo de sitio

        // Crear los objetos layout porque se van a usar y queda mas limpio :)
        val layoutNodoPadre = findViewById<ConstraintLayout>(R.id.layoutDatosHijo)
        val layoutNodoHijo = findViewById<ConstraintLayout>(R.id.layoutDatosPadre)

        findViewById<RadioButton>(R.id.radioNodoPadre).setOnClickListener {
            findViewById<TextView>(R.id.textCantidadNodos).visibility = View.GONE
            findViewById<EditText>(R.id.cantidadNodos).visibility = View.GONE
            layoutNodoPadre.visibility = View.VISIBLE
            layoutNodoHijo.visibility = View.GONE

        }
        findViewById<RadioButton>(R.id.radioNodoHijo).setOnClickListener {
            findViewById<TextView>(R.id.textCantidadNodos).visibility = View.GONE
            findViewById<EditText>(R.id.cantidadNodos).visibility = View.GONE
            layoutNodoPadre.visibility = View.VISIBLE
            layoutNodoHijo.visibility = View.VISIBLE
        }
        findViewById<RadioButton>(R.id.radioDDos).setOnClickListener {
            findViewById<TextView>(R.id.textCantidadNodos).visibility = View.VISIBLE
            findViewById<EditText>(R.id.cantidadNodos).visibility = View.VISIBLE
            layoutNodoPadre.visibility = View.GONE
            layoutNodoHijo.visibility = View.VISIBLE
//            val padre = findViewById<ConstraintLayout>(R.id.layoutDatosPadre)
//            val layoutParams = padre.layoutParams as ConstraintLayout.LayoutParams
//            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
//            padre.layoutParams = layoutParams
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}