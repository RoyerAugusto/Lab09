
package com.example.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenPosts(
    navController: NavHostController,
    servicio: PostApiService
) {
    val listaPosts: SnapshotStateList<PostModel> =
        remember { mutableStateListOf() }

    LaunchedEffect(Unit) {
        val listado = servicio.getUserPosts()
        listaPosts.clear()
        listado.forEach {
            listaPosts.add(it)
        }
    }

    LazyColumn {
        items(listaPosts) { item ->
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = item.id.toString(),
                    modifier = Modifier.weight(0.05f),
                    textAlign = TextAlign.End
                )

                Spacer(
                    modifier = Modifier.padding(horizontal = 1.dp)
                )

                Text(
                    text = item.title,
                    modifier = Modifier.weight(0.7f)
                )

                IconButton(
                    onClick = {
                        navController.navigate("postsVer/${item.id}")
                        Log.e("POSTS", "ID = ${item.id}")
                    },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Text("Ver")
                }
            }
        }
    }
}

@Composable
fun ScreenPost(
    navController: NavHostController,
    servicio: PostApiService,
    id: Int
) {
    var post by remember {
        mutableStateOf<PostModel?>(null)
    }

    LaunchedEffect(id) {
        post = servicio.getUserPostById(id)
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (post != null) {
            OutlinedTextField(
                value = post!!.id.toString(),
                onValueChange = {},
                label = { Text("id") },
                readOnly = true
            )

            OutlinedTextField(
                value = post!!.userId.toString(),
                onValueChange = {},
                label = { Text("userId") },
                readOnly = true
            )

            OutlinedTextField(
                value = post!!.title,
                onValueChange = {},
                label = { Text("title") },
                readOnly = true
            )

            OutlinedTextField(
                value = post!!.body,
                onValueChange = {},
                label = { Text("body") },
                readOnly = true
            )
        }
    }
}
