package com.example.stillzeit.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddMealManuallyScreen(
    viewModel: AddMealManuallyViewModel,
    modifier: Modifier = Modifier
) {
    // Um den Text dynamisch anpassen zu könne, benötigen
    // wir die breite des Bildschirms
    val configuration = LocalConfiguration.current
    // 600dp ist in Android ein typsicher Schwellenwert für mittlere Bildschirmgrößen
    // wie Tablets
    val isLargeScreen = configuration.screenWidthDp >= 600
    when (viewModel.page) {
        MealTypePage.MENU -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Button(
                    onClick = { viewModel.setAddMealPage(MealTypePage.BREAST) },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSpacing)
                    )
                    Text(
                        stringResource(
                            if (isLargeScreen) R.string.add_feeding_manually_expanded
                            else R.string.add_feeding_manually
                        )
                    )
                }
            }
        }
        MealTypePage.BREAST -> {
            AddBreastFeedingMealManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBreastFeedingMealManuallyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    DateField(
                        R.string.date,
                        ZonedDateTime.now(),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    TimeField(
                        R.string.time,
                        ZonedDateTime.now(),
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            DurationField(
                label = R.string.duration_in_minutes,
                value = ""
            )
        }
    }
}

@Composable
fun DateField(
    @StringRes label: Int,
    value: ZonedDateTime,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    if (showPicker) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeField(
    @StringRes label: Int,
    value: ZonedDateTime,
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
fun DurationField(
    @StringRes label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(value) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("0") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
