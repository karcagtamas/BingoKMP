package eu.karcags.bingo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import eu.karcags.bingo.database.DesktopDBConstructor
import eu.karcags.bingo.repository.PersistentBingoRepository

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Bingo",
    ) {
        val database = DesktopDBConstructor().initialize()
        val repository = PersistentBingoRepository(database.bingoDao())
        App(repository)
    }
}