package me.tunaxor.apps.mandadin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotePage
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotesPage
import me.tunaxor.apps.mandadin.ui.components.TodoPage
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm
import me.tunaxor.apps.mandadin.vm.TodoPageVm


fun NavGraphBuilder.fsFediverseNotes(navController: NavController, iFsFed: IFsFedNotes) {
    val vm = FsFedNotesVm(service = iFsFed.iFsFedNotes)
    navigation(startDestination = "notes", route = "fediverse") {
        composable(
            "notes?page={page}&limit={limit}", arguments = listOf(
                navArgument("page") { defaultValue = 1; type = NavType.IntType },
                navArgument("limit") { defaultValue = 10; type = NavType.IntType },
            )
        ) {
            LaunchedEffect(true) {
                vm.fetchNotes(
                    page = it.arguments?.getInt("page") ?: 1,
                    limit = it.arguments?.getInt("limit") ?: 10
                )
            }
            FsFediverseNotesPage(vm = vm)
        }
        composable(
            "notes/{note}", arguments = listOf(navArgument("note") { type = NavType.StringType })
        ) {
            FsFediverseNotePage(it.arguments?.getString("page"), vm)
        }
    }
}

fun NavGraphBuilder.todos(navController: NavController, todos: ITodos) {
    val vm = TodoPageVm(todos.iTodos)
    composable("todos") {
        TodoPage(vm)
    }
}


@Composable
fun AppShell(appEnv: ApplicationEnv) {
    MandadinTheme {
        val ctrl = rememberNavController()
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = ctrl, startDestination = "todos") {
                todos(ctrl, appEnv)
                fsFediverseNotes(navController = ctrl, appEnv)
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val env: ApplicationEnv = AppEnv()
        setContent { AppShell(env) }
    }
}
