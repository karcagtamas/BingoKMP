package eu.karcags.bingo.repository

import eu.karcags.bingo.model.FixedTile
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.model.Preset
import eu.karcags.bingo.model.Tile
import kotlinx.serialization.json.Json
import kotlin.random.Random
import kotlin.time.Clock

class BingoRepository {
    private val _presets = mutableListOf<Preset>()
    private val _games = mutableListOf<Game>()

    init {
        _presets.add(
            Preset(
                id = "default_classic",
                name = "Classic 5x5 Grid",
                matrixSize = 5,
                fixedTiles = listOf(FixedTile(2, 2, "FREE SPACE")),
                poolTiles = (1..60).map { "Tiles $it" },
            )
        )
    }

    fun getPresets(): List<Preset> = _presets
    fun getGames(): List<Game> = _games

    fun importPresetFromJson(json: String): Result<Preset> {
        return runCatching {
            val preset = Json.decodeFromString<Preset>(json)
            _presets.add(preset)
            preset
        }
    }

    fun createGame(preset: Preset): Game {
        val size = preset.matrixSize
        val shuffledPool = preset.poolTiles.shuffled().toMutableList()
        val fixedMap = preset.fixedTiles.associateBy { it.row to it.col }

        val grid = List(size) { row ->
            List(size) { col ->
                val fixed = fixedMap[row to col]

                if (fixed != null) {
                    Tile(id = "fixed_${row}_${col}", label = fixed.label, isChecked = false, isFixed = true)
                } else {
                    val label = if (shuffledPool.isNotEmpty()) shuffledPool.removeAt(0) else ""
                    Tile(id = "rand_${row}_${col}_${Random.nextInt()}", label = label, isChecked = false)
                }
            }
        }

        val newGame = Game(
            id = "game_${Clock.System.now().toEpochMilliseconds()}",
            presetId = preset.id,
            presetName = preset.name,
            matrixSize = size,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            grid = grid,
        )
        _games.add(newGame)
        return newGame
    }

    fun toggleTile(gameId: String, row: Int, col: Int): Game? {
        val idx = _games.indexOfFirst { it.id == gameId }
        if (idx == -1) return null

        val game = _games[idx]
        val updatedGrid = game.grid.mapIndexed { r, gridRow ->
            gridRow.mapIndexed { c, tile ->
                if (r == row && c == col) {
                    tile.copy(isChecked = !tile.isChecked)
                } else tile
            }
        }

        val isWon = checkWinCondition(updatedGrid, game.matrixSize)
        val updatedGame = game.copy(grid = updatedGrid, isWon = isWon)
        _games[idx] = updatedGame
        return updatedGame
    }

    private fun checkWinCondition(grid: List<List<Tile>>, size: Int): Boolean {
        if (grid.any { row -> row.all { it.isChecked } }) return true
        for (col in 0 until size) {
            if ((0 until size).all { r -> grid[r][col].isChecked }) return true
        }
        if ((0 until size).all { i -> grid[i][i].isChecked }) return true
        if ((0 until size).all { i -> grid[i][size - 1 - i].isChecked }) return true
        return false
    }
}