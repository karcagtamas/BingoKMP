package eu.karcags.bingo

import androidx.compose.runtime.Composable
import eu.karcags.bingo.repository.BingoRepository
import eu.karcags.bingo.ui.BingoApp

@Composable
fun App(bingoRepository: BingoRepository) {
    BingoTheme {
        BingoApp(
            repository = bingoRepository
        )
    }
}