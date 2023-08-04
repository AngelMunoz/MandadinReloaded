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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import kotlinx.coroutines.flow.map
import me.tunaxor.apps.mandadin.types.Routes
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotePage
import me.tunaxor.apps.mandadin.ui.components.FsFediverseNotesPage
import me.tunaxor.apps.mandadin.ui.components.FsNavPagination
import me.tunaxor.apps.mandadin.ui.components.TodoPage
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.FsFedNotesVm
import me.tunaxor.apps.mandadin.vm.TodoPageVm

fun NavGraphBuilder.fsFediverseNotes(nav: NavController, vm: FsFedNotesVm) {
    navigation(route = "fediverse", startDestination = "notes") {
        composable(
            route = Routes.FediverseNotes,
            arguments = listOf(navArgument("page") { type = NavType.IntType; defaultValue = 10 },
                navArgument("limit") { type = NavType.IntType; defaultValue = 1 }),
        ) {
            LaunchedEffect(true) {
                vm.loadNotes()
            }
            FsFediverseNotesPage(nav, vm)
        }
        composable(
            route = Routes.FediverseNote,
            arguments = listOf(navArgument("note") { type = NavType.StringType; nullable = true }),
        ) {
            FsFediverseNotePage(
                it.arguments?.getString("note"),
                vm,
                onBackRequested = { nav.navigateUp() }) { noteId ->
                nav.navigate("notes/$noteId")
            }
        }

    }
}

fun NavGraphBuilder.todos(iTodos: ITodos) {
    val vm = TodoPageVm(iTodos.Todos)
    composable(route = Routes.Home) {
        TodoPage(vm)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(appEnv: ApplicationEnv) {
    MandadinTheme {
        val ctrl = rememberNavController()
        val route = remember {
            ctrl.currentBackStackEntryFlow.map { entry -> entry.destination.route }
        }.collectAsState(initial = null)
        val fediverseVm =
            rememberUpdatedState(newValue = FsFedNotesVm(service = appEnv.FsFediverse))

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
        }, bottomBar = {
            if (route.value == Routes.FediverseNotes) {
                FsNavPagination(vm = fediverseVm.value)
            }
        }) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(navController = ctrl, startDestination = "todos") {
                    todos(iTodos = appEnv)
                    fsFediverseNotes(nav = ctrl, vm = fediverseVm.value)
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
