package com.example.stillzeit.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.stillzeit.R
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
                modifier = modifier
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
            }
        }
        MealTypePage.BREAST -> {
            AddBreastFeedingMealManuallyScreen(
                onBack = { viewModel.setAddMealPage(MealTypePage.MENU) }
            )
        }
    }

}

@Composable
fun AddBreastFeedingMealManuallyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }
        }
        item {
            DateField(
                "Start",
                ZonedDateTime.now(),
                onClick = { }
            )
        }
    }
}

@Composable
fun DateField(
    label: String,
    value: ZonedDateTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.titleSmall
        )

        OutlinedTextField(
            value = value.format(DateTimeFormatter.ISO_LOCAL_DATE),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        )
    }
}