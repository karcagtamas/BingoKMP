package eu.karcags.bingo.ui

import eu.karcags.bingo.model.Game

sealed interface Screen {

    object MainMenu : Screen
    object PresetList : Screen
    object GameList : Screen
    data class GamePlay(val game: Game) : Screen
}