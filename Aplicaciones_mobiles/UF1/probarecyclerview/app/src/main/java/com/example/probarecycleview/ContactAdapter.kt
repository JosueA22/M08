package com.example.ej1clase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.probarecycleview.R

class ContactAdapter(private val mContacts: List<Contact>): RecyclerView.Adapter<ContactAdapter.ViewHolder>() { //Clase ContafctAdapter con variable mContact : List<Contact>

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { //Creamos una clase interna llamada ViewHolder con parametros de tipo view
        val nameTextView: TextView = itemView.findViewById<TextView>(R.id.contact_name) //Usando los identificadores de xml guardamos los elementos en una variable
        val messageButton = itemView.findViewById<Button>(R.id.message_button)  //Usando los identificadores de xml guardamos los elementos en una variable
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {  //Metodo con parametros del padre (ViewGroup) y un viewType que es un int
        // Se llama por primera vez (Al principio)
        val context = parent?.context   // Cogemos el contexto del padre
        val inflater = LayoutInflater.from(context) //Usamos el inflater
        val contactView = inflater.inflate(R.layout.item_contact, parent, false)

        return ViewHolder(contactView)  //Devolvemos la lista al viewHolder
    }

    override fun getItemCount(): Int {
        return mContacts.size   //Devuelve el tamaño de mContact
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { //Metodo que se llama al actualizar el viewHolder
        val contact : Contact = mContacts.get(position) //Guardamos en contact el elemento de esa posicion de la lista mContact
        val textView = holder.nameTextView
        textView.text = contact.name    // Ponemos el nombre del contacto en el textView
        holder.messageButton.text = if (contact.isOnline) "Online" else "Offline" // Cambiamos el texto en función de si esta online o no
        holder.messageButton.isEnabled = contact.isOnline
    }

}