package com.example.stillzeit.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
    onBack: () -> Unit,
    viewModel: MealDetailViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()
    val meal = uiState.value.meal
    val isEditing = uiState.value.isEditing
    LazyColumn(horizontalAlignment = Alignment.Start, modifier = modifier) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button_content_description)
                    )
                }
                IconButton(onClick = {
                    if (isEditing) viewModel.save() else viewModel.startEditingComment()
                }) {
                    Icon(
                        imageVector = if (isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = stringResource(
                            if (isEditing) R.string.save else R.string.edit
                        )
                    )
                }
            }
        }
        item {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier) {
                            Text(stringResource(R.string.date))
                            Text(text = meal?.formattedStartDate() ?: "", modifier = Modifier)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier) {
                            Text(stringResource(R.string.time_Label))
                            Text(text = meal?.formattedStartTime() ?: "", modifier = Modifier)
                        }
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier) {
                        Text(stringResource(R.string.duration_label))
                        Text(text = meal?.formattedDuration() ?: "", modifier = Modifier)
                    }
                }
            }
        }
        item {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.comment_label))
                    if (isEditing) {
                        OutlinedTextField(
                            value = uiState.value.updatedComment,
                            onValueChange = viewModel::updateCommentDraft,
                            placeholder = { Text(stringResource(R.string.comment_placeholder)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(meal?.comment ?: "")
                    }
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