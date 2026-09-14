package eu.karcags.bingo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.bingo.repository.BingoRepository
import eu.karcags.bingo.ui.BingoApp

@Composable
@Preview
fun App() {
    MaterialTheme {
        BingoApp(
            repository = BingoRepository()
        )
    }
}