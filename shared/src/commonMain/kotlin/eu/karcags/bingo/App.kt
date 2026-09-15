package eu.karcags.bingo

import androidx.compose.runtime.Composable
import eu.karcags.bingo.repository.PersistentBingoRepository
import eu.karcags.bingo.ui.BingoApp

@Composable
fun App(bingoRepository: PersistentBingoRepository) {
    BingoTheme {
        BingoApp(
            repository = bingoRepository
        )
    }
}