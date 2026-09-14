package eu.karcags.bingo

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.bingo.repository.BingoRepository
import eu.karcags.bingo.ui.BingoApp

@Composable
@Preview
fun App() {
    BingoTheme {
        BingoApp(
            repository = BingoRepository()
        )
    }
}