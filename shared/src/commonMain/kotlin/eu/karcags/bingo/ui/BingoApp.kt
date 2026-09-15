package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import eu.karcags.bingo.repository.PersistentBingoRepository

@Composable
fun BingoApp(
    repository: PersistentBingoRepository,
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KMP Bingo",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                navigationIcon = {
                    if (currentScreen != Screen.MainMenu) {
                        TextButton(onClick = { currentScreen = Screen.MainMenu }) {
                            Text(
                                text = "< Home",
                                color = MaterialTheme.colorScheme.primary,
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
                    currentScreen = Screen.GamePlay(it)
                }

                is Screen.GameList -> GameListScreen(
                    repository = repository,
                ) {
                    currentScreen = Screen.GamePlay(it)
                }

                is Screen.GamePlay -> GamePlayScreen(screen.game, repository) {
                    currentScreen = Screen.GameList
                }
            }
        }
    }
}