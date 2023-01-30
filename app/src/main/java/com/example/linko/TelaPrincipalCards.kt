package com.example.linko

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

//Composição da tela principal
@Composable
fun TelaCards(navController: NavController) {
    val items = listOf(ItemNavegacao.Cards, ItemNavegacao.Tarefas, ItemNavegacao.Perfil)

    val user = Firebase.auth.currentUser
    val uid = user?.uid
    val db = FirebaseDatabase.getInstance()

    var titulos by remember {mutableStateOf(emptyList<String>())}

    db.reference.child(uid.toString()).child("titulosCards")
        .addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val titulosList = mutableListOf<String>()
            snapshot.children.forEach {
                titulosList.add(it.value as String)
            }
            titulos.value = titulosList
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("Erro", "$error")
        }
    })

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
            titulos.forEach { title ->
                TitlesCards(title)
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
fun TitlesCards(title: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {})) {
        Card(
            shape = RoundedCornerShape(8.dp)
        ) {
            Surface(
                color = MaterialTheme.colors.surface
            ) {
                Text(text = title)
            }
        }
    }
}

//Composição da tela adicionar cards
@Composable
fun TelaAdicionarCartao(navController: NavController) {

    val items = listOf(ItemNavegacao.Cards, ItemNavegacao.Tarefas, ItemNavegacao.Perfil)
    var titulo by remember { mutableStateOf("") }
    val context = LocalContext.current

    val dadosRef = MainViewModel()

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
                            val sucesso = dadosRef.adicionarCartao(titulo)
                            if (sucesso == 1){
                                Toast.makeText(context, "Sucesso!", Toast.LENGTH_SHORT).show()
                                navController.navigate(ItemNavegacao.Cards.route)
                            } else {
                                Toast.makeText(context, "Algo deu errado!", Toast.LENGTH_SHORT).show()
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


//classe para a navegação funcional dos botões da parte inferior da tela
sealed class ItemNavegacao(val route: String, val label: String, val icon: ImageVector){
    object Cards : ItemNavegacao(route = "telaCards", label = "Cards", icon = Icons.Default.Home)
    object Tarefas : ItemNavegacao(route = "telaTarefas", label = "Tarefas", icon = Icons.Default.EditNote)
    object Perfil : ItemNavegacao(route = "telaPerfil", label = "Perfil", icon = Icons.Default.Person)
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    LinkoTheme {
        TelaCards(rememberNavController())
    }
}*/