package com.raywenderlich.treasurehuntapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.raywenderlich.treasurehuntapp.navigation.AppNavigation
import com.raywenderlich.treasurehuntapp.ui.theme.TreasureHuntAppTheme
import com.raywenderlich.treasurehuntapp.viewModel.TreasureHuntViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreasureHuntAppTheme {
                val treasureHuntViewModel: TreasureHuntViewModel =
                    viewModel()

                AppNavigation(
                    viewModel = treasureHuntViewModel
                )
            }
        }
    }
}
