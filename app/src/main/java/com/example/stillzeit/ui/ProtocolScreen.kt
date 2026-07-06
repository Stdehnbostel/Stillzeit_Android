package com.example.stillzeit.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import com.example.stillzeit.data.Day
import com.example.stillzeit.data.Meal
import java.time.DayOfWeek
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ProtocolScreen(
    viewModel: ProtocolViewModel,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.decrementWeek() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Week"
                )
            }
            Text(
                text = stringResource(
                    R.string.date_range,
                    uiState.value.startOfWeek,
                    uiState.value.endOfWeek
                ),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = { viewModel.incrementWeek() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Week"
                )
            }
        }
        SingleChoiceSegmentedButton(
            onCheckedChange = viewModel::showList,
            showList = uiState.value.showList,
            modifier = Modifier.fillMaxWidth()
        )
        if (uiState.value.showList) {
            MealList(
                meals = uiState.value.meals,
                onMealClick = onMealClick,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        } else {
            MealDiagram(
                days = uiState.value.days,
                onMealClick = onMealClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

/**
 * Weekly diagram: seven weekday columns (Monday..Sunday) with the time of day on the
 * vertical axis (00:00 top, 24:00 bottom). Each meal is drawn as a tappable bar in the
 * accent color; tapping a bar invokes [onMealClick] with the meal's uuid.
 */
@Composable
fun MealDiagram(
    days: List<Day>,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = MaterialTheme.colorScheme.secondary
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (dayOfWeek in DayOfWeek.entries) {
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        MealDiagramCanvas(
            days = days,
            accentColor = accentColor,
            onMealClick = onMealClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun MealDiagramCanvas(
    days: List<Day>,
    accentColor: Color,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .pointerInput(days) {
                val gapWidth = 8.dp.toPx()
                val minMealHeight = 2.dp.toPx()
                detectTapGestures { tap ->
                    mealRects(
                        days = days,
                        canvasWidth = size.width.toFloat(),
                        canvasHeight = size.height.toFloat(),
                        gapWidth = gapWidth,
                        minMealHeight = minMealHeight
                    )
                        // last drawn is on top, so match it when meals overlap
                        .lastOrNull { it.second.contains(tap) }
                        ?.let { onMealClick(it.first.uuid.toString()) }
                }
            },
    ) {
        val stripeCount = 7
        val sectionCount = 24
        val gapWidth = 8.dp.toPx()

        val totalGapWidth = gapWidth * (stripeCount - 1)
        val stripeWidth = (size.width - totalGapWidth) / stripeCount
        val sectionHeight = size.height / sectionCount

        for (stripeIndex in 0 until stripeCount) {
            val stripeX = stripeIndex * (stripeWidth + gapWidth)
            for (sectionIndex in 0 until sectionCount) {
                val color = if (sectionIndex % 2 == 0) {
                    Color.LightGray
                } else {
                    Color.Gray
                }
                drawRect(
                    color = color,
                    topLeft = Offset(stripeX, sectionIndex * sectionHeight),
                    size = Size(stripeWidth, sectionHeight)
                )
            }
        }

        mealRects(
            days = days,
            canvasWidth = size.width,
            canvasHeight = size.height,
            gapWidth = gapWidth,
            minMealHeight = 2.dp.toPx()
        ).forEach { (_, rect) ->
            drawRect(
                color = accentColor,
                topLeft = rect.topLeft,
                size = rect.size
            )
        }
    }
}

/**
 * Computes the rectangle for every meal on the weekly diagram, so that drawing and
 * tap hit-testing share the exact same geometry. Columns are weekdays (Monday -> 0,
 * Sunday -> 6) and the vertical axis is the time of day (00:00 top, 24:00 bottom).
 */
private fun mealRects(
    days: List<Day>,
    canvasWidth: Float,
    canvasHeight: Float,
    gapWidth: Float,
    minMealHeight: Float
): List<Pair<Meal, Rect>> {
    val stripeCount = 7
    val minutesPerDay = 24f * 60f
    val totalGapWidth = gapWidth * (stripeCount - 1)
    val stripeWidth = (canvasWidth - totalGapWidth) / stripeCount

    val rects = mutableListOf<Pair<Meal, Rect>>()
    for (day in days) {
        val stripeIndex = day.start.dayOfWeek.value - 1
        if (stripeIndex !in 0 until stripeCount) continue
        val stripeX = stripeIndex * (stripeWidth + gapWidth)

        for (meal in day.meals) {
            val endInstant = meal.end?.let { ZonedDateTime.parse(it) } ?: meal.startAsDate()

            val startMinutes = Duration.between(day.start, meal.startAsDate())
                .toMinutes()
                .coerceIn(0L, minutesPerDay.toLong())
            val endMinutes = Duration.between(day.start, endInstant)
                .toMinutes()
                .coerceIn(startMinutes, minutesPerDay.toLong())

            val top = startMinutes / minutesPerDay * canvasHeight
            val mealHeight = ((endMinutes - startMinutes) / minutesPerDay * canvasHeight)
                .coerceAtLeast(minMealHeight)

            rects.add(meal to Rect(stripeX, top, stripeX + stripeWidth, top + mealHeight))
        }
    }
    return rects
}

@Composable
fun MealList(
    meals: List<Meal>,
    onMealClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(meals) {
            meal -> MealCard(
            meal,
            onClick = { onMealClick(meal.uuid.toString()) },
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
            )
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(
    showList: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val options = listOf(stringResource(R.string.list_view).toString(), stringResource(R.string.diagramm_view).toString())
    val selectedIndex = if (showList) 0 else 1

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onCheckedChange(index == 0) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.meal_description, meal.formattedStartTime(), meal.formattedEndTime(), meal.formattedDuration()),
            modifier = Modifier.padding(16.dp)
        )
    }
}
