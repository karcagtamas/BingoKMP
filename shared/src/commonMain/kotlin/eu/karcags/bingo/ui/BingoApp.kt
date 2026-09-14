package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import eu.karcags.bingo.repository.BingoRepository

@Composable
fun BingoApp(
    repository: BingoRepository,
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KMP Bingo") },
                navigationIcon = {
                    if (currentScreen != Screen.MainMenu) {
                        TextButton(onClick = { currentScreen = Screen.MainMenu }) {
                            Text(
                                text = "< Home,",
                                color = Color.White,
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            when (val screen = currentScreen) {
                is Screen.MainMenu -> MainMenuScreen(
                    { currentScreen = Screen.PresetList },
                    { currentScreen = Screen.GameList },
                )

                is Screen.PresetList -> PresetListScreen(
                    repository = repository,
                ) {
                    currentScreen = Screen.GamePlay(it.id)
                }

                is Screen.GameList -> GameListScreen(
                    repository = repository,
                ) {
                    currentScreen = Screen.GamePlay(it)
                }

                is Screen.GamePlay -> GamePlayScreen(screen.gameId, repository) {
                    currentScreen = Screen.GameList
                }
            }
        }
    }
}