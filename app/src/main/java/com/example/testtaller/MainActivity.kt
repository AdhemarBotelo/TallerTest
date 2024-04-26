package com.example.testtaller

import LoginState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.testtaller.ui.theme.TestTallerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTallerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginForm()

                }
            }
        }
    }
}

suspend fun onLogin(user: String, pwd: String): Boolean {
    delay(1000)
    return user == "admin" && pwd == "admin"
}

@Composable
fun LoginForm() {
    var state by remember {
        mutableStateOf(LoginState())
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.usuario))
        OutlinedTextField(
            value = state.userName, onValueChange = { state = state.copy(userName = it) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Password")
        OutlinedTextField(
            value = state.password, onValueChange = { state = state.copy(password = it) },
            modifier = Modifier.fillMaxWidth()
        )
        val lifeCycleOwner = LocalLifecycleOwner.current
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            state = state.copy(isLoading = true)
            state = state.copy(error = false)
            lifeCycleOwner.lifecycleScope.launch {
                var result = onLogin(state.userName, state.password)
                state = state.copy(error = !result)
                state = state.copy(isLogued = result)
                state = state.copy(isLoading = false)
            }
        }) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.White)
            } else {
                Text(text = stringResource(R.string.login))
            }
        }

        if (state.error) {
            Text(text = stringResource(R.string.usuario_o_pwd_incorrect))
        } else if (state.isLogued) {
            Text(text = stringResource(R.string.usuario_logueado))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    TestTallerTheme {
        LoginForm()
    }
}