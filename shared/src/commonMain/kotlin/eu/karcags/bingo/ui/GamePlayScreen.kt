package eu.karcags.bingo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.repository.PersistentBingoRepository
import kotlinx.coroutines.launch

@Composable
fun GamePlayScreen(
    initialGame: Game,
    repository: PersistentBingoRepository,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var game by remember { mutableStateOf(initialGame) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(game.presetName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        if (game.isWon) {
            Text(
                text = "BINGO!",
                color = Color(0xFF2E7D32),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(game.matrixSize),
                modifier = Modifier
                    .padding(4.dp),
            ) {
                items(game.matrixSize * game.matrixSize) { i ->
                    val r = i / game.matrixSize
                    val c = i % game.matrixSize
                    val tile = game.grid[r][c]

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(if (tile.isChecked) Color(0xFFBBDEFB) else Color.White)
                            .clickable {
                                scope.launch {
                                    game = repository.toggleTile(game, r, c)
                                }
                            },
                        border = BorderStroke(1.dp, Color.LightGray),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(4.dp),
                        ) {
                            Text(
                                text = tile.label,
                                fontSize = 11.sp,
                                fontWeight = if (tile.isFixed) FontWeight.Bold else FontWeight.Normal,
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text("Save & Close Board")
        }
    }
}