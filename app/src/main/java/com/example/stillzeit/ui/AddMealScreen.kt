package com.example.stillzeit.ui

import android.util.Log
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
fun AddMealSreen(
    viewModel: AddMealViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    MeasureFeedingTime(
        viewModel::setMealStart,
        viewModel::endMeal,
        viewModel::endFeeding,
        uiState.meal?.start ?: "",
        uiState.meal?.end ?: "",
        uiState.meal?.uuid,
        modifier = modifier
    )
}

// Element, das die zwei Timer Buttons für links, bzw. rechts enthält sowie den Button zum Beenden der Mahlzeit
@OptIn(ExperimentalUuidApi::class)
@Composable
fun MeasureFeedingTime(
    startMeal: (String) -> Unit,
    endMeal: () -> Unit,
    endFeeding: (Feeding, BreastFeedingDetail) -> Unit,
    mealStartText: String,
    mealEndText: String,
    mealId: UUID?,
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
                startMeal,
                endFeeding,
                mealId,
                modifier = modifier
            )
            DynamicTimer(
                R.string.right_feeding_button_name,
                "right",
                startMeal,
                endFeeding,
                mealId,
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
    startMeal: (String) -> Unit,
    endFeeding: (Feeding, BreastFeedingDetail) -> Unit,
    mealId: UUID?,
    modifier: Modifier = Modifier
) {
    var feeding by rememberSaveable { mutableStateOf(false) }

    var start by rememberSaveable { mutableStateOf(getSystemTimeStamp()) }
    var end by rememberSaveable { mutableStateOf(getSystemTimeStamp()) }

    var durationString by rememberSaveable { mutableStateOf("00:00:00") }

    // Einfache Timer Implementierung. Muss später angepasst werden.
    LaunchedEffect(start) {
        Log.d("coroutineContext", "started Timer")
        while (feeding) {
            durationString = updateTimeString(start)
            Log.d("coroutineContext", "updeted Timer")
        }
        Log.d("coroutineContext", "ended Timer")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val now = getSystemTimeStamp()
                if (feeding) {
                    end = now
                    val endTimeString = createTimeString(now)

                    durationString = createDurationString(start, end)
                    if (mealId != null) {
                        val newFeeding = Feeding(
                            id = UUID.randomUUID(), volume = 0, mealId = mealId,
                            type = "breast"
                        )
                        val detail = BreastFeedingDetail(
                            0, start = createTimeString(start), end = endTimeString, active = false,
                            side = side,
                            feedingId = newFeeding.id
                        )
                        endFeeding(newFeeding, detail)
                    }
                } else {
                    start = now
                    val startTimeString = createTimeString(now)
                    durationString = "00:00:00"
                    startMeal(startTimeString)
                }
                feeding = !feeding
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

@VisibleForTesting
internal fun createTimeString(zonedDateTime: ZonedDateTime): String {
    // DateTimeFormatter erfordert API leven 26.
    // Da Zeitstemple für diese App von zentraler Bedeutung sind
    // stellt dies die untere Schrnake der zu Unterstützenden
    // Android Versionen dar. (Android 8.0)
    val locale = Locale.GERMANY
    val formatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.MEDIUM) // Dieses Format ist für die Anziege geeignet.
        // für die Datenbank: .ISO_INSTANT
        .withLocale(locale)
    return zonedDateTime.format(formatter)
}

@VisibleForTesting
internal fun createTimeStringForDatabase(zonedDateTime: ZonedDateTime): String {
    // DateTimeFormatter erfordert API leven 26.
    // Da Zeitstemple für diese App von zentraler Bedeutung sind
    // stellt dies die untere Schrnake der zu Unterstützenden
    // Android Versionen dar. (Android 8.0)
    val locale = Locale.GERMANY
    val formatter = DateTimeFormatter
        .ISO_INSTANT // Zur Speicherung in der Datenbank wird das .ISO_Instant-Format benötigt
        .withLocale(locale)
    return zonedDateTime.format(formatter)
}

// Für das Label des Buttons. Später soll die Anzeige laufend aktualisiert werden.
@VisibleForTesting
internal fun createDurationString(start: ZonedDateTime, end: ZonedDateTime): String {
    var duration = Duration.between(start, end)
    // umwandlung in KotlinDuration, da meine Duration Umsetzung
    // Api level 31 benötigt hätte. Nicht sehr elegant. Wahrscheinlich geht das shöner.
    val kotlinDuration = duration.toKotlinDuration()
    var seconds = kotlinDuration.inWholeSeconds
    val hours = seconds / 3600
    seconds -= hours * 3600
    val minutes = seconds / 60
    seconds -= minutes * 60

    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

suspend fun updateTimeString(start: ZonedDateTime): String {
    delay(500)
    val now = getSystemTimeStamp()
    return createDurationString(start, now)
}