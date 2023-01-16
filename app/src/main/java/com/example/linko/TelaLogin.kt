package com.example.linko

import android.content.ContentValues
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun TelaLogin(navController: NavController) {

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf ("") }

    val isEmailValid by derivedStateOf {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isPasswordValid by derivedStateOf {
        senha.length >= 8
    }

    var isPasswordVisible by remember { mutableStateOf(false)}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .background(Color.Unspecified)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_sem_fundo),
            contentDescription = null
        )
        TextField(
            label = { Text(stringResource(R.string.email)) },
            placeholder = {Text("abc@hostname.com")},
            singleLine = true,
            value = email,
            onValueChange = { email = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            isError = !isEmailValid,
            trailingIcon = {
                if (email.isNotBlank()) {
                    IconButton(onClick = {email = ""}) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        TextField(
            label = { Text(stringResource(R.string.senha)) },
            singleLine = true,
            value = senha,
            onValueChange = { senha = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus()}
            ),
            isError = !isPasswordValid,
            trailingIcon = {
                IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                    Icon(
                        imageVector = if(isPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (email.isNotEmpty() && senha.isNotEmpty()) {
                    val auth = Firebase.auth

                    auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show()
                                navController.navigate(ItemNavegacao.Cards.route)
                            } else {
                                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(context, "criação de usuário falhou", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            },
            enabled = isEmailValid && isPasswordValid
        ){
            Text(stringResource(id = R.string.entrar))
        }
        TextButton(
            onClick = { navController.navigate("telaCadastrarUsuario")}
        ) {
            Text(stringResource(id = R.string.cadastrar))
        }
    }
}