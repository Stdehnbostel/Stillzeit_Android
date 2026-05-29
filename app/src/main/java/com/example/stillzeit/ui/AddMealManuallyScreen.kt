package com.example.stillzeit.ui

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.stillzeit.R
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment

@Composable
fun AddMealManuallyScreen(
    modifier: Modifier = Modifier
) {
    // Um den Text dynamisch anpassen zu könne, benötigen
    // wir die breite des Bildschirms
    val configuration = LocalConfiguration.current
    // 600dp ist in Android ein typsicher Schwellenwert für mittlere Bildschirmgrößen
    // wie Tablets
    val isLargeScreen = configuration.screenWidthDp >= 600
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            onClick = { /*TODO*/ },
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