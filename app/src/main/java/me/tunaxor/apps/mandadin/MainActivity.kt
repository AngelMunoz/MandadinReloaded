package me.tunaxor.apps.mandadin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import me.tunaxor.apps.mandadin.ui.components.TodoPage
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.TodoVm


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val env = AppEnv()
        val todoVm = TodoVm(env.iTodos)
        setContent {
            MandadinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TodoPage(todoVm)
                }
            }
        }
    }
}
