package eu.karcags.bingo.ui

import eu.karcags.bingo.model.Game
import eu.karcags.bingo.model.Preset

sealed interface Screen {

    object MainMenu : Screen
    object PresetList : Screen
    object GameList : Screen
    data class GamePlay(val game: Game) : Screen
    data class PresetEditor(val editingPreset: Preset? = null) : Screen
}