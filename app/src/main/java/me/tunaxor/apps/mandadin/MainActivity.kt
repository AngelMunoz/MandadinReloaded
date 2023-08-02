package me.tunaxor.apps.mandadin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotePage
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotesPage
import me.tunaxor.apps.mandadin.ui.components.TodoPage
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm
import me.tunaxor.apps.mandadin.vm.TodoPageVm

fun NavGraphBuilder.fsFediverseNotes(nav: NavController, env: IFsFediverse) {
    val vm = FsFedNotesVm(service = env.FsFediverse)
    navigation(route = "fediverse", startDestination = "notes") {
        composable(
            route = "notes?page={page}&limit={limit}",
            arguments = listOf(navArgument("page") { type = NavType.IntType; defaultValue = 10 },
                navArgument("limit") { type = NavType.IntType; defaultValue = 1 }),
        ) {
            LaunchedEffect(true) {
                vm.fetchNotes(
                    page = it.arguments?.getInt("page") ?: 1,
                    limit = it.arguments?.getInt("limit") ?: 10
                )
            }
            FsFediverseNotesPage(nav, vm)
        }
        composable(
            route = "notes/{note}",
            arguments = listOf(navArgument("note") { type = NavType.StringType; nullable = true }),
        ) {
            FsFediverseNotePage(it.arguments?.getString("note"), vm)
        }

    }
}

fun NavGraphBuilder.todos(iTodos: ITodos) {
    val vm = TodoPageVm(iTodos.Todos)
    composable(route = "todos") {
        TodoPage(vm)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(appEnv: ApplicationEnv) {
    MandadinTheme {
        val ctrl = rememberNavController()
        // A surface container using the 'background' color from the theme
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Mandadin",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }, actions = {
                IconButton(onClick = {
                    ctrl.navigate("todos")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Todos",
                        modifier = Modifier.padding(4.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = {
                    ctrl.navigate("fediverse")
                }) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Fediverse Notes",
                        modifier = Modifier.padding(4.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            })
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = ctrl, startDestination = "todos") {
                    todos(iTodos = appEnv)
                    fsFediverseNotes(nav = ctrl, env = appEnv)
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val env: ApplicationEnv = AppEnv(BuildConfig.SERVER_URL)
        setContent { AppShell(env) }
    }
}
