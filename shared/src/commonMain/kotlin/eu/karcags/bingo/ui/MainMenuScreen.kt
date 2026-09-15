package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    onNavPresets: () -> Unit,
    onNavGames: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Bingo Engine",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNavPresets,
            modifier = Modifier
                .fillMaxWidth(0.5f),
        ) {
            Text("Presets & Import")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavGames,
            modifier = Modifier.fillMaxWidth(0.5f),
        ) {
            Text("Active Boards")
        }
    }
}