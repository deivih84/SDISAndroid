package com.miau.sdisandroid

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miau.sdisandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ip: String
    private var puerto: Int = 12345
    private lateinit var socket: Socket
    private lateinit var input: InputStream
    private lateinit var output: OutputStream

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MensajeAdapter
    private val mensajes = mutableListOf<Mensaje>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cliente)

        recyclerView = findViewById(R.id.recyclerChat)
        adapter = MensajeAdapter(mensajes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        findViewById<ImageButton>(R.id.buttonEnviar).isEnabled = false
    }


    // Primera Vista


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

    // FUNCIONES VISTA 2


    private fun inicializar(serverIp: String, puerto: Int) {
        try {
            socket = Socket(serverIp, puerto)
            output = socket.getOutputStream()
            input = socket.getInputStream()
            println(socket)

        } catch (e: Exception) {
            println("ERRORES: $e")
        }

    }

    @SuppressLint("StaticFieldLeak")
    private inner class InitTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            // Pasarle a inicializar la ip especificada y el puerto en el que se va a conectar
            inicializar(findViewById<TextView>(R.id.editTextConexion).text.toString(), puerto)
            println("Zocalo creado con exito")

            // Cositas del reciclerview

            return null
        }
    }

    @SuppressLint("CutPasteId")




    fun procesarEnviarPulsado(view: View) {
        findViewById<ImageButton>(R.id.buttonEnviar).isEnabled = false
        print("Envía un mensaje al servidor: ") // En realidad pasará antes por el intermediario

        // Accede al RecyclerView ya inflado
        var editTextConsulta = findViewById<TextView>(R.id.editTextConsulta).text
        val texto = editTextConsulta.toString()
        if (texto.isBlank()) { // No enviar mensajes que esten en blanco!!😡
            findViewById<ImageButton>(R.id.buttonEnviar).isEnabled = true
            return
        }
        findViewById<TextView>(R.id.editTextConsulta).text = ""

        // Agregar un nuevo mensaje especificando el usuario y texto
        addNewMensaje("Usuario", texto)


        GlobalScope.launch(Dispatchers.IO) {
            enviarMensaje(texto)
        }

        // Leer nuevo mensaje respuesta que mande el intermediario
        GlobalScope.launch(Dispatchers.IO) {
            recibirMensaje()
        }
        findViewById<ImageButton>(R.id.buttonEnviar).isEnabled = true
    }


    //2
    private fun recibirMensaje() {
        val buffer = ByteArray(1024)
        val bytesRead = input.read(buffer)
        CoroutineScope(Dispatchers.Main).launch {
            addNewMensaje("Gepeto", String(buffer, 0, bytesRead))
        }
        println(String(buffer, 0, bytesRead))
    }


    private fun enviarMensaje(texto: String) {
        output.write(texto.toByteArray())
        output.flush()
    }


    // Función para agregar un nuevo mensaje y actualizar el RecyclerView
    private fun addNewMensaje(autor: String, texto: String) {
        val imagenSrc = if (autor == "Gepeto") 1 else 0
        val nuevoMensaje = Mensaje(autor, texto, imagenSrc)
        adapter.addMensaje(nuevoMensaje)
        recyclerView.scrollToPosition(mensajes.size - 1)
    }


    fun procesarConexionPulsado(view: View) {
        findViewById<TextView>(R.id.labelErrores).text = "Creando el socket :)"
        InitTask().execute()
        Thread.sleep(1000)

        if (socket == null){
            findViewById<TextView>(R.id.labelErrores)
            println("Fallo al crear el socket")
        }
        else {
            findViewById<Button>(R.id.buttonConexion).visibility = View.GONE
            findViewById<EditText>(R.id.editTextConexion).visibility = View.GONE
            findViewById<TextView>(R.id.labelErrores).visibility = View.GONE
            findViewById<ImageButton>(R.id.buttonEnviar).isEnabled = true
        }
    }




}


