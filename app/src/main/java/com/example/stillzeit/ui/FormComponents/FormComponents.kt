package com.example.stillzeit.ui.FormComponents

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.stillzeit.R
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    @StringRes label: Int,
    value: ZonedDateTime,
    setTime: (ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = value.hour,
        initialMinute = value.minute,
        is24Hour = true
    )

    if (showPicker) {
        AlertDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val updatedDateTime = value.withHour(timePickerState.hour)
                        .withMinute(timePickerState.minute)
                    setTime(updatedDateTime)
                    showPicker = false
                }) {
                    Text(
                        text = stringResource(R.string.ok)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPicker = false
                }) {
                    Text(
                        text = stringResource(R.string.abort_action)
                    )
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleSmall
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value.format(DateTimeFormatter.ofPattern("HH:mm")),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            // Overlay Box to intercept clicks
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = {
                        showPicker = true
                        Log.i("TimePicker", "TimePicker overlay clicked")
                    })
            )
        }
    }
}

@Composable
fun AmountField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("0") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DateField(
    @StringRes label: Int,
    value: ZonedDateTime,
    setDate: (ZonedDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    if (showPicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                        val updatedDateTime = value.withYear(selectedDate.year)
                            .withMonth(selectedDate.monthValue)
                            .withDayOfMonth(selectedDate.dayOfMonth)
                        setDate(updatedDateTime)
                    }
                    showPicker = false
                }) {
                    Text(
                        text = stringResource(R.string.ok)
                    )
                }
            }
        ) {
            DatePicker(state = state)
        }
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleSmall
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    showPicker = true
                    Log.i("DatePicker", "DatePicker clicked")
                })
        ) {
            OutlinedTextField(
                value = value.format(DateTimeFormatter.ISO_LOCAL_DATE),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            // Overlay Box to intercept clicks
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = {
                        showPicker = true
                        Log.i("DatePicker", "DatePicker overlay clicked")
                    })
            )
        }
    }
}