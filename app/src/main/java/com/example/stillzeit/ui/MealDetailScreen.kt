package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.stillzeit.R

@Composable
fun MealDetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: MealDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val meal = uiState.value.meal
    LazyColumn(horizontalAlignment = Alignment.Start, modifier = modifier) {
        item {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }
        }
        item {
            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier) {
                    Text(stringResource(R.string.date))
                    Text(text = meal?.formattedStartDate() ?: "", modifier = Modifier)
                }
                Column(modifier = Modifier) {
                    Text(stringResource(R.string.time_Label))
                    Text(text = meal?.formattedStartTime() ?: "", modifier = Modifier)
                }
            }
        }
        item {
            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier) {
                    Text(stringResource(R.string.duration_label))
                    Text(text = meal?.formattedDuration() ?: "", modifier = Modifier)
                }
            }
        }
        item {
            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier) {
                    Text(stringResource(R.string.comment_label))
                    Text(meal?.comment ?: "")
                }
            }
        }
        item {
            Button(onClick = {}, modifier = Modifier) {
                Text(stringResource(R.string.delete_meal))
            }
        }
    }
}