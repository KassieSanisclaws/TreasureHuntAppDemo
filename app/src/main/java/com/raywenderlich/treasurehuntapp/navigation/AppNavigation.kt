package com.raywenderlich.treasurehuntapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raywenderlich.treasurehuntapp.ui.theme.appUI.HomeScreen
import com.raywenderlich.treasurehuntapp.ui.theme.appUI.HuntScreen
import com.raywenderlich.treasurehuntapp.ui.theme.appUI.CompletionScreen
import com.raywenderlich.treasurehuntapp.viewModel.TreasureHuntViewModel


object Routes {

    const val HOME = "home"
    const val HUNT = "hunt"
    const val COMPLETE = "complete"
}

@Composable
fun AppNavigation(
    viewModel: TreasureHuntViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {

            HomeScreen(
                onStartHunt = {
                    viewModel.resetHunt()
                    navController.navigate(Routes.HUNT)
                }
            )
        }

        composable(Routes.HUNT) {

            HuntScreen(
                viewModel = viewModel,
                onHuntComplete = {
                    navController.navigate(Routes.COMPLETE) {
                        popUpTo(Routes.HUNT) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.COMPLETE) {

            CompletionScreen(
                onRestart = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.COMPLETE) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}