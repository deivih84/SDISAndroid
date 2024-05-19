package com.miau.sdisandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Definimos el adaptador para el RecyclerView
class MensajeAdapter(private val mensajes: MutableList<Mensaje>) : RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {

    // Definimos el ViewHolder que contendrá las vistas del layout del mensaje
    class MensajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorTextView: TextView = view.findViewById(R.id.autorTextView) // Referencia al TextView del autor
        val messageTextView: TextView = view.findViewById(R.id.mensajeTextView) // Referencia al TextView del mensaje
    }

    // Inflamos el layout del mensaje y creamos un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mensaje_view0, parent, false) //////////////////////////////////////////////////////////////
        return MensajeViewHolder(view)
    }

    // Vinculamos los datos del mensaje a las vistas del ViewHolder
    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.authorTextView.text = mensaje.autor
        holder.messageTextView.text = mensaje.texto
    }

    // Devolvemos el número total de mensajes
    override fun getItemCount() = mensajes.size

    // Añadimos un nuevo mensaje a la lista y notificamos al adaptador
    fun addMensaje(mensaje: Mensaje) {
        mensajes.add(mensaje)
        notifyItemInserted(mensajes.size - 1)
    }
}