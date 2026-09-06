package com.raywenderlich.treasurehuntapp.ui.theme.appUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raywenderlich.treasurehuntapp.viewModel.TreasureHuntViewModel

@Composable
fun HomeScreen(
    viewModel: TreasureHuntViewModel,
    onStartHunt: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Treasure Hunt",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Explore your city and discover 20 local destinations.",
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Start at City Hall and follow each clue.",
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = onStartHunt,
            modifier = Modifier.padding(top = 32.dp)
        ) {

            Text("Start Hunt")
        }
    }
}