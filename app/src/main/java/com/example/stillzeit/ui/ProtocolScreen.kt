package com.example.stillzeit.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.stillzeit.R
import com.example.stillzeit.data.Meal

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
            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                val canvasQuadrantSize = size / 8F
                drawRect(
                    color = Color.Gray,
                    size = canvasQuadrantSize
                )
            }
        }
    }
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
    val options = listOf("Liste", "Diagramm")
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
