package com.example.linko

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.linko.ui.theme.LinkoTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//https://medium.com/@daniel.atitienei/how-to-read-data-from-firebase-realtime-database-and-display-it-using-jetpack-compose-fdc0316009ea

@Composable
fun TelaCards(navController: NavController) {

    val items = listOf(ItemNavegacao.Cards, ItemNavegacao.Tarefas, ItemNavegacao.Perfil)

    Scaffold(
        topBar = { TopAppBar(title = { Text(ItemNavegacao.Cards.label) }) },
        floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("TelaAdicionarCartao") },
                    content = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
        },
        content = {
            Button(
                onClick = {
                    val db = Firebase.database.reference
                    val user = Firebase.auth.currentUser
                    val uid = user?.uid

                    db.child(uid.toString()).child("titulosCards").get()
                        .addOnSuccessListener {
                            Log.i("firebase","Dados pegos ${it.value}")
                            val lista = it.value.toString().replace(", ","=")
                            val novaLista = lista.split("=")
                            //val tipo = lista::class.simpleName
                            Log.i("tipoDADO", "Array é $lista")

                            for(i in 1.. novaLista.size step 2){
                                val item = novaLista[i]
                                Log.i("Vetor", "$item")
                            }
                        }.addOnFailureListener{
                            Log.e("firebase", "Erro Ao tentar recuperar dados", it)
                        }
                }
            ) {
                Text(text = "Verificar Dados")
            }
        },
        bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colors.background){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach {
                    BottomNavigationItem(
                        selected = currentRoute == it.route,
                        label = {
                            Text(text = it.label, color = if(currentRoute==it.route) Color.Blue else Color.LightGray)
                        },
                        icon = {
                            Icon(imageVector = it.icon, contentDescription = null,
                                tint = if(currentRoute == it.route) Color.Blue else Color.LightGray)
                        },
                        onClick = {
                            navController.navigate(it.route)
                        }
                    )
                }
            }
        },
    )//fim do scaffold
}//fim da tela cards

@Composable
fun TelaAdicionarCartao(navController: NavController) {

    val items = listOf(ItemNavegacao.Cards, ItemNavegacao.Tarefas, ItemNavegacao.Perfil)
    var titulo by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Adicionar") }) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                TextField(
                    placeholder = {Text("Título do deck")},
                    value = titulo,
                    onValueChange = { titulo = it },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (titulo.isNotBlank()) {
                            val database = Firebase.database.reference
                            val user = Firebase.auth.currentUser
                            val uid = user?.uid

                            database.child(uid.toString()).child("titulosCards").push().setValue(titulo)
                                .addOnCompleteListener() { task ->
                                    if(task.isSuccessful) {
                                        Toast.makeText(context, "Sucesso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(ItemNavegacao.Cards.route)
                                    } else {
                                        Log.w(TAG, "Erro", task.exception)
                                    }
                                }

                        } else {
                            Toast.makeText(context, "Preencha o campo!", Toast.LENGTH_SHORT).show()
                        }

                    }
                ) {
                    Text(text = "Criar")
                }
            }
        },
        bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colors.background){
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach {
                    BottomNavigationItem(
                        selected = currentRoute == it.route,
                        label = {
                            Text(text = it.label, color = if(currentRoute==it.route) Color.Blue else Color.LightGray)
                        },
                        icon = {
                            Icon(imageVector = it.icon, contentDescription = null,
                                tint = if(currentRoute == it.route) Color.Blue else Color.LightGray)
                        },
                        onClick = {
                            navController.navigate(it.route)
                        }
                    )
                }
            }
        },
    )//fim do scaffold
}//fim da tela cards

sealed class ItemNavegacao(val route: String, val label: String, val icon: ImageVector){
    object Cards : ItemNavegacao(route = "telaCards", label = "Cards", icon = Icons.Default.Home)
    object Tarefas : ItemNavegacao(route = "telaTarefas", label = "Tarefas", icon = Icons.Default.EditNote)
    object Perfil : ItemNavegacao(route = "telaPerfil", label = "Perfil", icon = Icons.Default.Person)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    LinkoTheme {
        TelaCards(rememberNavController())
    }
}