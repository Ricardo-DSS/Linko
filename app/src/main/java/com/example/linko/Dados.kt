package com.example.linko

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    private val db = Firebase.database.reference
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid

    val titles = MutableLiveData<List<String>>()

    suspend fun listaDeTitulos() {
        withContext(Dispatchers.IO) {
            val materia = mutableListOf<String>()
            db.child(uid.toString()).child("titulosCards").get().await().apply {
                val lista = value.toString().replace(", ","=")
                val novaLista = lista.split("=")
                for(i in 1.. novaLista.size step 2){
                    materia.add(novaLista[i])
                }
            }
            titles.postValue(materia)
        }
    }

    fun adicionarCartao(titulo: String): Int{
        var sucesso = 0
        db.child(uid.toString()).child("titulosCards").push().setValue(titulo)
            .addOnSuccessListener() { task ->
                sucesso = 1
            }
        return sucesso
    }
}