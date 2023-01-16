package com.example.linko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linko.ui.theme.LinkoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavigationGraph()
                }
            }
        }
    }
}

//função responsável pela navegação entre as telas
@Composable
fun NavigationGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "telaLogin"
    ) {
        composable(route = "telaLogin") {
            TelaLogin(navController = navController)
        }
        composable(route = "telaCadastrarUsuario") {
            TelaCadastroUsuario(navController = navController)
        }
        composable(route = ItemNavegacao.Cards.route) {
            TelaCards(navController = navController)
        }
        composable(route = ItemNavegacao.Tarefas.route) {
            TelaTarefas(navController = navController)
        }
        composable(route = ItemNavegacao.Perfil.route) {
            TelaPerfil(navController = navController)
        }
        composable(route = "TelaAdicionarCartao"){
            TelaAdicionarCartao(navController = navController)
        }
    }
}

