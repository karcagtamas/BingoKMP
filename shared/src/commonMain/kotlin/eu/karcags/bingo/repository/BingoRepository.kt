package eu.karcags.bingo.repository

import eu.karcags.bingo.model.Game
import eu.karcags.bingo.model.Preset

interface BingoRepository {

    suspend fun getPresets(): List<Preset>
    suspend fun getGames(): List<Game>
    suspend fun savePreset(preset: Preset)
    suspend fun createGameFromPreset(preset: Preset): Game
    suspend fun toggleTile(game: Game, row: Int, col: Int): Game

    fun exportPresetToJson(preset: Preset): String
}