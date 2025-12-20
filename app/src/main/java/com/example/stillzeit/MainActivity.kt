package com.example.stillzeit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stillzeit.data.BreastFeedingDao
import com.example.stillzeit.data.BreastFeeding
import com.example.stillzeit.ui.theme.StillzeitTheme
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.Duration
import kotlin.time.toKotlinDuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        setContent {
            StillzeitTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MeasureFeedingTime(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun MeasureFeedingTime(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            DynamicTimer(R.string.left_feeding_button_name, modifier = modifier)
            DynamicTimer(R.string.right_feeding_button_name, modifier = modifier)
        }
        Button(onClick = { }) {
            Text(stringResource(R.string.finish_meal_button))
        }
    }
}

@Composable
fun DynamicTimer(
    @StringRes sideString: Int,
    modifier: Modifier = Modifier
) {
    var feeding by rememberSaveable { mutableStateOf(false) }

    var start by rememberSaveable { mutableStateOf(getSystemTimeStamp()) }
    var end by rememberSaveable { mutableStateOf(getSystemTimeStamp()
    ) }
    var startTimeString by rememberSaveable { mutableStateOf("") }
    var endTimeString by rememberSaveable { mutableStateOf("") }
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
        DisplayTimeText(
            prefix = R.string.start_time,
            timeString = startTimeString,
            modifier = modifier
        )
        DisplayTimeText(
            prefix = R.string.end_time,
            timeString = endTimeString,
            modifier = modifier
        )
        Button(
            onClick = {
                val now = getSystemTimeStamp()
                if (feeding) {
                    end = now
                    endTimeString = createTimeString(now)
                    durationString = createDurationString(start, end)
                } else {
                    start = now
                    startTimeString = createTimeString(now)
                    durationString = "00:00:00"
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
                modifier = modifier
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
        MeasureFeedingTime()
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
    val locale = java.util.Locale.GERMANY
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
    val locale = java.util.Locale.GERMANY
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

@Database(entities = [BreastFeeding::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breastFeedingDao(): BreastFeedingDao
}