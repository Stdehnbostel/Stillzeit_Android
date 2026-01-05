package com.example.stillzeit.ui

import android.util.Log
import androidx.annotation.BoolRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import com.example.stillzeit.ui.theme.StillzeitTheme
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import kotlin.time.toKotlinDuration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stillzeit.data.BreastFeeding
import com.example.stillzeit.data.BreastFeedingDetail
import com.example.stillzeit.data.Feeding
import java.util.Locale
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddMealScreen(
    viewModel: AddMealViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    MeasureFeedingTime(
        viewModel::endMeal,
        viewModel::processTimestampButton,
        viewModel::updateTimeString,
        uiState.meal?.start ?: "",
        uiState.meal?.end ?: "",
        uiState.feeding,
        viewModel.getLastFeeding()?.detail?.side ?: "",
        modifier = modifier
    )
}

// Element, das die zwei Timer Buttons für links, bzw. rechts enthält sowie den Button zum Beenden der Mahlzeit
@OptIn(ExperimentalUuidApi::class)
@Composable
fun MeasureFeedingTime(
    endMeal: () -> Unit,
    processTimestampButton: (String) -> String,
    createDurationString: () -> String,
    mealStartText: String,
    mealEndText: String,
    feeding: Boolean,
    lastFeedingSide: String,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        DisplayTimeText(
            prefix = R.string.start_time,
            timeString = mealStartText,
            modifier = Modifier
        )
        DisplayTimeText(
            prefix = R.string.end_time,
            timeString = mealEndText,
            modifier = Modifier
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            DynamicTimer(
                R.string.left_feeding_button_name,
                "left",
                processTimestampButton,
                createDurationString,
                feeding && (lastFeedingSide == "left"),
                modifier = modifier
            )
            DynamicTimer(
                R.string.right_feeding_button_name,
                "right",
                processTimestampButton,
                createDurationString,
                feeding && (lastFeedingSide == "right"),
                modifier = modifier)
        }
        Button(onClick = endMeal) {
            Text(stringResource(R.string.finish_meal_button))
        }
    }
}

// runder Button mit Timer
@Composable
fun DynamicTimer(
    @StringRes sideString: Int,
    side: String,
    processTimestampButton: (String) -> String,
    createDurationString: () -> String,
    feeding: Boolean,
    modifier: Modifier = Modifier
) {
    var durationString by rememberSaveable { mutableStateOf("00:00:00") }

    // Einfache Timer Implementierung. Muss später angepasst werden.
    LaunchedEffect(feeding) {
        Log.d("coroutineContext", "started Timer")
        while (feeding) {
            durationString = updateTimeString(createDurationString)
            Log.d("coroutineContext", "updated Timer")
        }
        Log.d("coroutineContext", "ended Timer")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                durationString = processTimestampButton(side)
            },
            modifier = Modifier
                /*
                diese Größe muss wahrscheinlich noch angepasst werden
                die Beschriftung der Buttons muss generell nochmal überdacht werden.
                */
                .size(dimensionResource(R.dimen.feedingTimerButton)),
            shape = CircleShape
        ) {
            Text(
                text = stringResource(R.string.timestamp_button, durationString),
                maxLines = 1,
                softWrap = false,
                modifier = Modifier
            )
        }
        Text(text = stringResource(sideString), modifier = modifier)
    }
}

@Composable
fun DisplayTimeText(
    @StringRes prefix: Int,
    timeString: String,
    modifier: Modifier = Modifier) {
    Text(
        text = stringResource(prefix, timeString),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    StillzeitTheme {
        AddMealSreen(
            viewModel = viewModel(),
            modifier = Modifier.fillMaxSize()
            )
    }
}

@VisibleForTesting
internal fun getSystemTimeStamp(): ZonedDateTime {
    val zoneId = ZoneId.systemDefault()
    return ZonedDateTime.now(zoneId) // Der erzeugte Zeitstempel, enthält auch Nanosekunden. Wir benötigen nur Sekunden.
        .truncatedTo(ChronoUnit.SECONDS)
}

suspend fun updateTimeString(createDurationString: () -> String): String {
    delay(500)
    return createDurationString()
}