package com.miau.sdisandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.miau.sdisandroid.LanzadorEpico.lanzador
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

    fun lanzarPulsado(view: View) {
        findViewById<FloatingActionButton>(R.id.fabCrearNodo).isClickable = false
        findViewById<FloatingActionButton>(R.id.fabCrearNodo).isFocusable = false

        val args = arrayOfNulls<String>(7) // CUIDADO nulls a la vista 😰
        args[0] = findViewById<EditText>(R.id.editNomHijo).text.toString()
        args[1] = findViewById<EditText>(R.id.editPortHijo).text.toString()
        args[2] = findViewById<EditText>(R.id.editTokHijo).text.toString()
        args[3] = findViewById<EditText>(R.id.editNomPadre).text.toString()
        args[4] = findViewById<EditText>(R.id.editPortPadre).text.toString()
        args[5] = findViewById<EditText>(R.id.editTokPadre).text.toString()
        args[6] = findViewById<EditText>(R.id.cantidadNodos).text.toString()

        // TODO Añadir una buena validacion para cada caso de servicio
        repeat(7) {
            if (args[it].isNullOrBlank()) args[it] = " "
        }

        val idRadioActivado = findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
//        println(idRadioActivado)
        val servicio: Utiles.Servicio = when (idRadioActivado) {
            R.id.radioNodoPadre -> Utiles.Servicio.NODOPADRE
            R.id.radioNodoHijo -> Utiles.Servicio.NODOHIJO
            R.id.radioDDos -> Utiles.Servicio.ATAQUEDDOS
            else -> Utiles.Servicio.INDEFINIDO
        }

        lanzador(args, servicio)
        findViewById<TextView>(R.id.editErrores).text = "Nodo Lanzado con éxito"
    }
}