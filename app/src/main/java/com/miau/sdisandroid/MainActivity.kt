package com.miau.sdisandroid

import android.net.http.HttpResponseCache.install
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import cn.dreampie.websocket.WebsocketClient
import com.firework.network.websocket.WebSocketClient
import java.time.*
import java.util.*
import kotlin.collections.LinkedHashSet
import com.miau.sdisandroid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ip: String
    private var puerto: Int = 0








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_cliente)




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

    fun procesarSubirPulsado(view: View) {
        mandarDatos(findViewById<EditText>(R.id.editTextConsulta).text.toString(), "hola")
    }

    fun mandarDatos(userText: String, data: String) {

    }



    fun configureSockets() {
        val cliente: dev.gustavoavila.websocketclient.WebSocketClient

    }


}