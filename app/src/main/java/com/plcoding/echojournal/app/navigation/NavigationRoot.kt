package com.plcoding.echojournal.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.plcoding.echojournal.echos.presentation.create_echo.CreateEchoRoot
import com.plcoding.echojournal.echos.presentation.echos.EchosRoot
import com.plcoding.echojournal.echos.presentation.echos.EchosScreen
import com.plcoding.echojournal.echos.presentation.settings.SettingsRoot
import com.plcoding.echojournal.echos.presentation.util.toCreateEchoRoute

const val ACTION_CREATE_ECHO="com.plcoding.echojournal.CREATE_ECHO"
@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Echos(startRecording = false)
    ) {
        composable<NavigationRoutes.Echos>(
            deepLinks = listOf(
                navDeepLink<NavigationRoutes.Echos>(
                    "https://echojournal.com/echos"
                ){
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
            EchosRoot(
                onNavigateToCreateEcho = {
                    details->
                    navController.navigate(details.toCreateEchoRoute())
                },
                onNavigateToSettings = {
                    navController.navigate(NavigationRoutes.Settings)
                }
            )
        }
        composable<NavigationRoutes.CreateEcho>{
            CreateEchoRoot(
                onConfirmLeave = navController::navigateUp
            )
        }
        composable<NavigationRoutes.Settings> {
            SettingsRoot(
                onGoBack = navController::navigateUp
            )
        }
    }
}