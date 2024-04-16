@file:OptIn(ExperimentalMaterial3Api::class)
package com.senac.boaviagemexercicio
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senac.boaviagemexercicio.components.*
import com.senac.boaviagemexercicio.model.Trip
import com.senac.boaviagemexercicio.model.TripType
import com.senac.boaviagemexercicio.ui.theme.BoaviagemexercicioTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoaviagemexercicioTheme {
                NewTripScreen()
            }
        }
    }
}
@Composable
fun NewTripScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    var trip by remember {
        mutableStateOf(
            Trip(
                destination = "",
                type = TripType.LAZER,
                startDate = Date(),
                endDate = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000),
                value = 0.0
            )
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { it
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan.copy(alpha = 0.1f))
                    .padding(16.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {}) {
                        Text("<--")
                    }
                    Text(
                        text = "Nova Viagem",
                        modifier = Modifier.weight(0.1f),
                        textAlign = TextAlign.Center
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    DestinationInput(destination = trip.destination, onDestinationChanged = { destination -> trip = trip.copy(destination = destination) })
                    TripTypeInput(tripType = trip.type, onTripTypeChanged = { type -> trip = trip.copy(type = type) })
                    ValueInput(value = formatValue(trip.value), onValueChanged = { newValue ->
                        trip = trip.copy(value = newValue.toDoubleOrNull() ?: 0.0)
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                    DatePickerComponent(
                        label = "Data de Início",
                        date = trip.startDate,
                        onDateSelected = { newDate ->
                            trip = trip.copy(startDate = newDate)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DatePickerComponent(
                        label = "Data de Término",
                        date = trip.endDate,

                        onDateSelected = { newDate ->
                            trip = trip.copy(endDate = newDate)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.Main).launch {
                                snackbarHostState.showSnackbar("Viagem Registrada")
                            }
                        },colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Salvar Viagem")
                    }
                }
            }
        }
    )
}
@Composable
fun formatValue(value: Double): String {
    val intValue = value.toInt()
    return if (value == intValue.toDouble()) {
        intValue.toString()
    } else {
        value.toString()
    }
}