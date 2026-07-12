package com.example.stillzeit.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import com.example.stillzeit.data.MealType
import com.example.stillzeit.ui.FormComponents.DateField
import com.example.stillzeit.ui.FormComponents.AmountField
import com.example.stillzeit.ui.FormComponents.DateTimePickerRow
import com.example.stillzeit.ui.FormComponents.TimeField
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

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
                modifier = modifier.padding(horizontal = 16.dp)
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
                Button(
                    onClick = { viewModel.setAddMealPage(MealTypePage.BOTTLE) },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSpacing)
                    )
                    Text(
                        stringResource(
                            if (isLargeScreen) R.string.add_bottle_feeding_manually_expanded
                            else R.string.add_bottle_feeding_manually
                        )
                    )
                }
                Button(
                    onClick = { viewModel.setAddMealPage(MealTypePage.OTHER) },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSpacing)
                    )
                    Text(
                        stringResource(
                            if (isLargeScreen) R.string.add_other_feeding_manually_expanded
                            else R.string.add_other_feeding_manually
                        )
                    )
                }
                Button(
                    onClick = { viewModel.setAddMealPage(MealTypePage.PUMPING) },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSpacing)
                    )
                    Text(
                        stringResource(
                            if (isLargeScreen) R.string.add_pumping_manually_expanded
                            else R.string.add_pumping_manually
                        )
                    )
                }
            }
        }
        MealTypePage.BREAST -> {
            AddBreastFeedingMealManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) },
                start = viewModel.start,
                duration = viewModel.duration,
                setStart = viewModel::setFeedingStart,
                setDuration = viewModel::setFeedingDuration,
                saveBreastFeeding = viewModel::saveMeal,
            )
        }

        MealTypePage.BOTTLE -> {
            AddBottleFeedingMealManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) },
                start = viewModel.start,
                volume = viewModel.volume,
                setStart = viewModel::setFeedingStart,
                setVolume = viewModel::setFeedingVolume,
                save = viewModel::saveMeal,
            )
        }
        MealTypePage.OTHER -> {
            AddOtherFeedingMealManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) },
                start = viewModel.start,
                amount = viewModel.volume,
                setStart = viewModel::setFeedingStart,
                setAmount = viewModel::setFeedingVolume,
                save = viewModel::saveMeal
            )
        }

        MealTypePage.PUMPING -> {
            AddPumpingManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) },
                start = viewModel.start,
                volume = viewModel.volume,
                setStart = viewModel::setFeedingStart,
                setVolume = viewModel::setFeedingVolume,
                save = viewModel::saveMeal
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBreastFeedingMealManuallyScreen(
    onBack: () -> Unit,
    start: ZonedDateTime,
    duration: kotlin.time.Duration,
    setStart: (ZonedDateTime) -> Unit,
    setDuration: (kotlin.time.Duration) -> Unit,
    saveBreastFeeding: (MealType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            BackButton(onBack, modifier = Modifier.fillMaxWidth())
        }
        item {
            DateTimePickerRow(
                dateLabel = R.string.date,
                timeLabel = R.string.time,
                start = start,
                setStart = setStart,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            var durationText by remember { mutableStateOf(if (duration == kotlin.time.Duration.ZERO) "" else duration.inWholeMinutes.toString()) }
            AmountField(
                label = R.string.duration_in_minutes,
                value = durationText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        durationText = newValue
                        setDuration((newValue.toLongOrNull() ?: 0L).minutes)
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { saveBreastFeeding(MealType.BREAST) }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun AddBottleFeedingMealManuallyScreen(
    onBack: () -> Unit,
    start: ZonedDateTime,
    volume: Int,
    setStart: (ZonedDateTime) -> Unit,
    setVolume: (Int) -> Unit,
    save: (MealType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            BackButton(onBack, modifier = Modifier.fillMaxWidth())
        }
        item {
            DateTimePickerRow(
                dateLabel = R.string.date,
                timeLabel = R.string.time,
                start = start,
                setStart = setStart,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            var volumeText by remember { mutableStateOf (volume.toString()) }
            AmountField(
                label = R.string.volume_label,
                value = volumeText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        volumeText = newValue
                        setVolume(newValue.toIntOrNull() ?: 0)
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { save(MealType.BOTTLE) }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun AddOtherFeedingMealManuallyScreen(
    onBack: () -> Unit,
    start: ZonedDateTime,
    amount: Int,
    setStart: (ZonedDateTime) -> Unit,
    setAmount: (Int) -> Unit,
    save: (MealType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            BackButton(onBack, modifier = Modifier.fillMaxWidth())
        }
        item {
            DateTimePickerRow(
                dateLabel = R.string.date,
                timeLabel = R.string.time,
                start = start,
                setStart = setStart,
            )
        }
        item {
            // Placeholder für komplexeres Teilmahlzeiten-Feld
            Spacer(modifier = Modifier.height(16.dp))
            var volumeText by remember { mutableStateOf (amount.toString()) }
            AmountField(
                label = R.string.volume_label,
                value = volumeText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        volumeText = newValue
                        setAmount(newValue.toIntOrNull() ?: 0)
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { save(MealType.OTHER) }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun AddPumpingManuallyScreen(
    onBack: () -> Unit,
    start: ZonedDateTime,
    volume: Int,
    setStart: (ZonedDateTime) -> Unit,
    setVolume: (Int) -> Unit,
    save: (MealType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            BackButton(onBack, modifier = Modifier.fillMaxWidth())
        }
        item {
            DateTimePickerRow(
                dateLabel = R.string.date,
                timeLabel = R.string.time,
                start = start,
                setStart = setStart,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            var volumeText by remember { mutableStateOf (volume.toString()) }
            AmountField(
                label = R.string.volume_label,
                value = volumeText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        volumeText = newValue
                        setVolume(newValue.toIntOrNull() ?: 0)
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { save(MealType.PUMPING) }) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun BackButton(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_button_content_description)
            )
        }
    }
}
