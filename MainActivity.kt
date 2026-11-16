package com.example.panaderiaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.panaderiaapp.ui.theme.PanaderiaAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PanaderiaAPPTheme {
                SistemaPanaderia()
            }
        }
    }
}

@Composable
fun SistemaPanaderia() {
    var pantallaActual by rememberSaveable { mutableStateOf("LOGIN") }

    var nombreRegistrado by rememberSaveable { mutableStateOf("Cliente") }
    var correoRegistrado by rememberSaveable { mutableStateOf("admin@panaderia.com") }
    var claveRegistrada by rememberSaveable { mutableStateOf("1234") }

    when (pantallaActual) {
        "LOGIN" -> PantallaLogin(
            correoGuardado = correoRegistrado,
            claveGuardada = claveRegistrada,
            onLoginExitoso = { pantallaActual = "INICIO" },
            onIrARegistro = { pantallaActual = "REGISTRO" }
        )
        "REGISTRO" -> PantallaRegistro(
            onRegistroCompletado = { nombre, correo, clave ->
                nombreRegistrado = nombre
                correoRegistrado = correo
                claveRegistrada = clave
                pantallaActual = "LOGIN"
            },
            onVolverALogin = { pantallaActual = "LOGIN" }
        )
        "INICIO" -> PanaderiaAPPApp(
            nombreUsuario = nombreRegistrado,
            onCerrarSesion = { pantallaActual = "LOGIN" }
        )
    }
}

@Composable
fun PantallaLogin(
    correoGuardado: String,
    claveGuardada: String,
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E3C72), Color(0xFF2A5298), Color(0xFFE67E22))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // LOGOTIPO / IMAGEN REPRESENTATIVA DE PANADERÍA
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF39C12)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_compass),
                        contentDescription = "Logo Panadería",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // TÍTULO COMERCIAL CLARO
                Text(
                    text = "Panadería APP",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD35400),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "¡El sabor tradicional en tus manos!",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (correo.trim() == correoGuardado && clave == claveGuardada) {
                            mensajeError = ""
                            onLoginExitoso()
                        } else {
                            mensajeError = "Correo o contraseña incorrectos"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD35400))
                ) {
                    Text("INICIAR SESIÓN", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onIrARegistro) {
                    Text("¿No tienes una cuenta? Regístrate aquí", color = Color(0xFF1E3C72), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(
    onRegistroCompletado: (String, String, String) -> Unit,
    onVolverALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E3C72), Color(0xFF2A5298), Color(0xFFE67E22))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nuevo Registro", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD35400))
                Text("Crea tu cuenta en Panadería APP", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError, color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (nombre.isNotBlank() && correo.isNotBlank() && clave.isNotBlank()) {
                            onRegistroCompletado(nombre.trim(), correo.trim(), clave)
                        } else {
                            mensajeError = "Por favor llena todos los campos"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27AE60))
                ) {
                    Text("REGISTRARSE", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onVolverALogin) {
                    Text("Volver al inicio de sesión", color = Color(0xFF1E3C72), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun PanaderiaAPPApp(nombreUsuario: String, onCerrarSesion: () -> Unit) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destino ->
                item(
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(destino.colorFondo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = destino.icon),
                                contentDescription = destino.label,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    label = { Text(destino.label, fontSize = 10.sp) },
                    selected = destino == currentDestination,
                    onClick = { currentDestination = destino }
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3C72),
                            Color(0xFF2A5298),
                            Color(0xFFE67E22)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Panadería APP",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bienvenido, $nombreUsuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sección: ${currentDestination.label}",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onCerrarSesion,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cerrar sesión", color = Color.White)
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
    val colorFondo: Color
) {
    HOME("Home", android.R.drawable.ic_menu_compass, Color(0xFF607D8B)),
    WHATSAPP("WhatsApp", android.R.drawable.ic_menu_send, Color(0xFF25D366)),
    FACEBOOK("Facebook", android.R.drawable.ic_menu_share, Color(0xFF1877F2)),
    MESSENGER("Messenger", android.R.drawable.ic_menu_edit, Color(0xFF00B2FF)),
    GMAIL("Gmail", android.R.drawable.ic_menu_help, Color(0xFFEA4335)),
    PROFILE("Profile", android.R.drawable.ic_menu_myplaces, Color(0xFFFF9800))
}
