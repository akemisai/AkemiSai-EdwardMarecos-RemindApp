package com.example.remindapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.remindapp.ui.theme.RemindAppTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemindAppTheme {
                setContent {
                    RemindApp()
                }
            }
        }
    }
}

@Composable
fun RemindApp() {
    var message by remember { mutableStateOf(TextFieldValue("")) }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var remindMessage by remember { mutableStateOf("") }
    var remindDate by remember { mutableStateOf("") }
    var remindTime by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Reminder Message") },
                modifier = Modifier.fillMaxWidth()
            )
            Row {
                Button(onClick = { datePickerDialog.show() }) {
                    Text(if (date.isEmpty()) "Select Date" else "Date: $date")
                }

                Button(onClick = { timePickerDialog.show() }) {
                    Text(if (time.isEmpty()) "Select Time" else "Time: $time")
                }
            }
            Row { ElevatedButton(
                onClick = {
                    if (message.text.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                        remindMessage = message.text
                        remindDate = date
                        remindTime = time

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Reminder Set: ${remindMessage}, $remindDate at $remindTime"
                            )
                        }
                    }
                },
                enabled = message.text.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()
            ) {
                Text("Set Reminder")
            }

                ElevatedButton(onClick = {
                    message = TextFieldValue("")
                    date = ""
                    time = ""
                    remindMessage = ""
                    remindDate = ""
                    remindTime = ""
                    scope.launch {
                        snackbarHostState.showSnackbar("Reminder Cleared")
                    }
                }) {
                    Text("Clear Reminder")
                } }
            if (remindMessage.isNotEmpty() && remindDate.isNotEmpty() && remindTime.isNotEmpty()) {
                ElevatedCard() {
                    Text(
                        text = "Reminder: $remindMessage, Date: $remindDate, Time: $remindTime",
                        modifier = Modifier.padding(16.dp)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemindAppPreview() {
    RemindApp()
}
