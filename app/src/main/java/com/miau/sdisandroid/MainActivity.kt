package com.miau.sdisandroid

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.miau.sdisandroid.databinding.ActivityMainBinding
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ip: String
    private var puerto: Int = 0
    private var socket: Socket? = null
    private lateinit var input: InputStream
    private lateinit var output: OutputStream


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_conexion)
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
            output = socket!!.getOutputStream()
            input = socket!!.getInputStream()

        } catch (e: Exception) {
            println("ERRORES: $e")
        }

    }

    @SuppressLint("StaticFieldLeak")
    private inner class InitTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            // Pasarle a inicializar la ip especificada y el puerto en el que se va a conectar
            inicializar(findViewById<TextView>(R.id.editTextConexion).text.toString(), 12345)
            println("Zocalo creado con exito")
            return null
        }
    }

    fun procesarEnviarPulsado(view: View) {
        print("Envía un mensaje al servidor: ")

        findViewById<RecyclerView>(R.id.recyclerChat).add
        val recyclerView: RecyclerView = findViewById(R.id.recyclerChat)
        val adapter = recyclerView.adapter as? ChatAdapter // Suponiendo que tu adaptador se llama ChatAdapter
        adapter?.addMessage(newMessage) // Método personalizado en tu adaptador para agregar un nuevo mensaje


//        val message = findViewById<TextView>(R.id.editTextConsulta).text.toString()
//        // Enviar el mensaje
//        output.write(message.toByteArray())
//        output.flush()
//
//        // Leer nuevo mensaje
//        val buffer = ByteArray(1024)
//        val bytesRead = input.read(buffer)
//        val response = String(buffer, 0, bytesRead)
//        println("Respuesta del servidor: $response")

    }


    fun procesarConexionPulsado(view: View) {
        findViewById<TextView>(R.id.labelErrores).text = "Creando el socket :)"
        InitTask().execute()

        if (socket == null)
            findViewById<TextView>(R.id.labelErrores)
        else {
            setContentView(R.layout.activity_cliente)
            println("Socket creado con exito :)")
        }
    }




}


class ChatAdapter(private val messages: List<String>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageContent.text = message.content
        holder.messageTime.text = message.time
        // Aquí puedes cargar la imagen de perfil utilizando alguna biblioteca de carga de imágenes como Glide o Picasso
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val messageContent: TextView = itemView.findViewById(R.id.messageContent)
        val messageTime: TextView = itemView.findViewById(R.id.messageTime)
    }
}
