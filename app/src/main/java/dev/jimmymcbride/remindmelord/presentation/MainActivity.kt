package dev.jimmymcbride.remindmelord.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.jimmymcbride.remindmelord.presentation.navigation.AppNavHost
import dev.jimmymcbride.remindmelord.presentation.ui.theme.RemindMeLordTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val seederViewModel: SeederViewModel by viewModels()
        seederViewModel.maybeSeedOnLaunch()
        setContent {
            RemindMeLordTheme {
                AppNavHost()
            }
        }
    }
}
