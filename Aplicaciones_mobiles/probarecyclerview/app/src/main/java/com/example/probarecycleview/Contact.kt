package com.example.ej1clase
import java.util.*

class Contact(val name: String, val isOnline: Boolean) { //Las variables de la clase va entre parentesis
    private var lastContactId = 0 //Contador

     fun createContactList(numContact: Int) : ArrayList<Contact> {  //Metodo
        val contact = ArrayList<Contact>() //Creamos un arrayList de Contact
        for(i in 1..numContact) { //Bucle for empezando en 1
            contact.add(Contact("Person " + ++lastContactId, i <=numContact / 2))
        }
         return contact //Devolvemos el ArrayList
     }
}