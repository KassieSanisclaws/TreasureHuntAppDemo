package com.raywenderlich.treasurehuntapp.ui.theme.appUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CompletionScreen(
    onRestart: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Treasure Hunt Complete!",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Congratulations! You visited all 20 locations."
        )

        Text(
            text = "You are now eligible to enter the vacation draw."
        )

        Button(
            onClick = onRestart
        ) {

            Text("Start Again")
        }
    }
}