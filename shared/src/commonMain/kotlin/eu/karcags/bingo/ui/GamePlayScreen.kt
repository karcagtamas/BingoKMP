package eu.karcags.bingo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    val tileSize = 130.dp
    val spacing = 8.dp

    val gridWidth = (tileSize * game.matrixSize) + (spacing * (game.matrixSize - 1))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(game.presetName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        if (game.isWon) {
            Text(
                "🎉 BINGO! 🎉",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                columns = GridCells.FixedSize(tileSize),
                modifier = Modifier
                    .width(gridWidth)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                items(game.matrixSize * game.matrixSize) { i ->
                    val r = i / game.matrixSize
                    val c = i % game.matrixSize
                    val tile = game.grid[r][c]

                    Card(
                        modifier = Modifier
                            .requiredSize(tileSize)
                            .clickable {
                                scope.launch {
                                    game = repository.toggleTile(game, r, c)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                tile.isPermanentlyToggled -> Color(0xFFE0E0E0)
                                tile.isChecked -> Color(0xFF630404).copy(alpha = 0.2f)
                                else -> Color.White
                            },
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (tile.isPermanentlyToggled) Color.Gray else Color.LightGray
                        ),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                if (tile.isPermanentlyToggled) {
                                    Text("🌟", fontSize = 12.sp, modifier = Modifier.padding(2.dp))
                                }

                                Text(
                                    text = tile.label,
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 5,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = if (tile.isFixed) FontWeight.Bold else FontWeight.Normal,
                                    color = if (tile.isPermanentlyToggled) Color.DarkGray else Color.Unspecified,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}