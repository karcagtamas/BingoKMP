package eu.karcags.bingo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.repository.BingoRepository

@Composable
fun GameListScreen(
    repository: BingoRepository,
    onSelectGame: (Game) -> Unit,
) {
    var games by remember { mutableStateOf<List<Game>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        games = repository.getGames()
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Active & Saved Matches",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (games.isEmpty()) {
            Text("No boards active. Create on from the Preset menu!")
        } else {
            LazyColumn {
                items(games) { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                onSelectGame(game)
                            },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                Text(
                                    text = "Preset: ${game.presetName}",
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = if (game.isWon) "BINGO!" else "In Progress",
                                    color = if (game.isWon) Color.Green else Color.Gray,
                                )
                            }
                            Text(
                                text = "Resume ->",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                            )
                        }
                    }
                }
            }
        }
    }
}