package eu.karcags.bingo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.repository.BingoRepository

@Composable
fun GamePlayScreen(
    gameId: String,
    repository: BingoRepository,
    onBack: () -> Unit,
) {
    var game by remember { mutableStateOf(repository.getGames().firstOrNull { it.id == gameId }) }
    if (game == null) {
        Text(text = "Game Error.")
        return
    }
    val activeGame = game!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(activeGame.presetName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        if (activeGame.isWon) {
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
                columns = GridCells.Fixed(activeGame.matrixSize),
                modifier = Modifier
                    .padding(4.dp),
            ) {
                items(activeGame.matrixSize * activeGame.matrixSize) { i ->
                    val r = i / activeGame.matrixSize
                    val c = i % activeGame.matrixSize
                    val tile = activeGame.grid[r][c]

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .background(if (tile.isChecked) Color(0xFFBBDEFB) else Color.White)
                            .clickable {
                                repository.toggleTile(activeGame.id, r, c)
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