package eu.karcags.bingo.ui

sealed interface Screen {

    object MainMenu : Screen
    object PresetList : Screen
    object GameList : Screen
    data class GamePlay(val gameId: Int) : Screen
}