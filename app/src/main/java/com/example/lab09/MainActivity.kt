package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ProgPrincipal9()
            }
        }
    }
}

@Composable
fun ProgPrincipal9() {
    val urlBase = "https://json-placeholder.mock.beeceptor.com/"

    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val servicio = remember(retrofit) {
        retrofit.create(PostApiService::class.java)
    }

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            BarraSuperior()
        },
        bottomBar = {
            BarraInferior(navController)
        }
    ) { paddingValues ->
        Contenido(
            paddingValues,
            navController,
            servicio
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text("JSONPlaceHolder Access")
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("inicio") {
                    launchSingleTop = true
                    popUpTo("inicio") {
                        inclusive = false
                    }
                }
            },
            icon = {
                Text("I")
            },
            label = {
                Text("Inicio")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate("posts") {
                    launchSingleTop = true
                }
            },
            icon = {
                Text("P")
            },
            label = {
                Text("Posts")
            }
        )
    }
}

@Composable
fun Contenido(
    paddingValues: PaddingValues,
    navController: NavHostController,
    servicio: PostApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") {
                ScreenInicio()
            }

            composable("posts") {
                ScreenPosts(navController, servicio)
            }

            composable(
                route = "postsVer/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0

                ScreenPost(
                    navController,
                    servicio,
                    id
                )
            }
        }
    }
}

@Composable
fun ScreenInicio() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "INICIO",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
