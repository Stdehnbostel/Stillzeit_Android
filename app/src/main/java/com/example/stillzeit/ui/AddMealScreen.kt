package com.example.stillzeit.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stillzeit.R
import com.example.stillzeit.data.Side
import com.example.stillzeit.ui.theme.StillzeitTheme
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
fun AddMealScreen(
    viewModel: AddMealViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val start = uiState.lastMeal?.start ?: ""
    val end = uiState.lastMeal?.end ?: ""
    val mealStartText = if (start != "") {
        viewModel.createTimeString(ZonedDateTime.parse(start))
    } else {
        start
    }
    val mealEndText = if (end != "") {
        viewModel.createTimeString(ZonedDateTime.parse(end))
    } else {
        end
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        MeasureFeedingTime(
            viewModel::endMeal,
            viewModel::processTimestampButton,
            viewModel::updateTimeString,
            uiState.feeding,
            viewModel.getLastFeeding()?.detail?.side?.side ?: Side.EMPTY.side,
            modifier = modifier
        )
        LastFeeding(
            mealStartText,
            mealEndText,
            modifier = modifier
        )
        OutlinedButton(onClick = { }, modifier = modifier) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Plus Symbol",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.add_manually))
        }
    }
}

// Element, das die zwei Timer Buttons für links, bzw. rechts enthält sowie den Button zum Beenden der Mahlzeit
@Composable
fun MeasureFeedingTime(
    endMeal: () -> Unit,
    processTimestampButton: (Side) -> String,
    createDurationString: () -> String,
    feeding: Boolean,
    lastFeedingSide: String,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            DynamicTimer(
                R.string.left_feeding_button_name,
                Side.LEFT,
                processTimestampButton,
                createDurationString,
                feeding && (lastFeedingSide == "left"),
                modifier = modifier
            )
            DynamicTimer(
                R.string.right_feeding_button_name,
                Side.RIGHT,
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

@Composable
fun LastFeeding(
    mealStartText: String,
    mealEndText: String,
    modifier: Modifier = Modifier
) {
    DisplayMealTimeText(
        startTimeString = mealStartText,
        endTimeString = mealEndText,
        modifier = Modifier
    )
}

// runder Button mit Timer
@Composable
fun DynamicTimer(
    @StringRes sideString: Int,
    side: Side,
    processTimestampButton: (Side) -> String,
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
fun DisplayMealTimeText(
    startTimeString: String,
    endTimeString: String,
    modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.start_end, startTimeString, endTimeString),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    StillzeitTheme {
        AddMealScreen(
            viewModel = viewModel(),
            modifier = Modifier.fillMaxSize()
            )
    }
}

suspend fun updateTimeString(createDurationString: () -> String): String {
    delay(500)
    return createDurationString()
}