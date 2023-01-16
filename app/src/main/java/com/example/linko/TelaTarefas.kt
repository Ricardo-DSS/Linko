package com.example.linko

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun TelaTarefas(navController: NavController){

    val items = listOf(ItemNavegacao.Cards, ItemNavegacao.Tarefas, ItemNavegacao.Perfil)

    Scaffold(
        topBar = { TopAppBar(title = { Text(ItemNavegacao.Tarefas.label) }) },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "Tarefas")
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
}//fim da tela tarefas