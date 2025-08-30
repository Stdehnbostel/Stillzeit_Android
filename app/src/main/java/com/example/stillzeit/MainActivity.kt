package com.example.stillzeit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.stillzeit.ui.theme.StillzeitTheme
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StillzeitTheme {
                // In Tutorials (auch offizielle von Google) wird hier Surface anstelle von Scaffold verwendet
                // Surface ist veraltet, da aktuelle "Foldable" Modelle nicht gut unterstützt wrden.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MeasureFeedingTime(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MeasureFeedingTime(modifier: Modifier = Modifier) {
    var feeding by remember { mutableStateOf(false) }

    var startTimeString by remember { mutableStateOf("") }
    var endTimeString by remember { mutableStateOf("") }

    Column(
        modifier = modifier
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
                if (feeding) {
                    endTimeString = createTimeString()
                } else {
                    startTimeString = createTimeString()
                }
                feeding = !feeding
            },
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.timestamp_button),
                modifier = modifier
            )
        }
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
internal fun createTimeString(): String {
    // die Java-Klasse Instant erzeugt standardmäßig einen Zeitstempel,
    // der auch Nanosekunden enthält. Wir benötigen nur Sekunden.
    val instant = Instant.now().truncatedTo(ChronoUnit.SECONDS)
    // DateTimeFormatter erfordert API leven 26.
    // Da Zeitstemple für diese App von zentraler Bedeutung sind
    // stellt dies die untere Schrnake der zu Unterstützenden
    // Android Versionen dar. (Android 8.0)
    return DateTimeFormatter
        .ISO_INSTANT
        .withZone(ZoneOffset.UTC)
        .format(instant)
}