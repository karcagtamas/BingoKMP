package eu.karcags.bingo.repository

import eu.karcags.bingo.database.BingoDao
import eu.karcags.bingo.database.GameEntity
import eu.karcags.bingo.database.PresetEntity
import eu.karcags.bingo.model.FixedTile
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.model.Preset
import eu.karcags.bingo.model.Tile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.Clock

class PersistentBingoRepository(private val dao: BingoDao) {

    suspend fun getPresets(): List<Preset> = withContext(Dispatchers.IO) {
        val list = dao.getAllPresets()

        if (list.isEmpty()) {
            val defaultPreset = Preset(
                id = "default_classic",
                name = "Classic 5x5 Grid",
                matrixSize = 5,
                fixedTiles = listOf(FixedTile(2, 2, "FREE SPACE")),
                poolTiles = (1..50).map { "Item $it" }
            )
            savePreset(defaultPreset)
            listOf(defaultPreset)
        } else {
            list.map {
                Preset(it.id, it.name, it.matrixSize, it.fixedTiles, it.poolTiles)
            }
        }
    }

    suspend fun getGames(): List<Game> = withContext(Dispatchers.IO) {
        dao.getAllGames().map {
            Game(it.id, it.presetId, it.presetName, it.matrixSize, it.createdAt, it.grid, it.isWon)
        }
    }

    suspend fun savePreset(preset: Preset) = withContext(Dispatchers.IO) {
        dao.insertPreset(PresetEntity(preset.id, preset.name, preset.matrixSize, preset.fixedTiles, preset.poolTiles))
    }

    suspend fun createGameFromPreset(preset: Preset): Game = withContext(Dispatchers.IO) {
        val size = preset.matrixSize
        val shuffledPool = preset.poolTiles.shuffled().toMutableList()
        val fixedMap = preset.fixedTiles.associateBy { it.row to it.col }

        val grid = List(size) { row ->
            List(size) { col ->
                val fixed = fixedMap[row to col]
                if (fixed != null) {
                    Tile(id = "fixed_${row}_${col}", label = fixed.label, isChecked = false, isFixed = true)
                } else {
                    val label = if (shuffledPool.isNotEmpty()) shuffledPool.removeAt(0) else "Empty Slot"
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

        dao.insertGame(
            GameEntity(
                newGame.id,
                newGame.presetId,
                newGame.presetName,
                newGame.matrixSize,
                newGame.createdAt,
                newGame.grid,
                newGame.isWon,
            )
        )
        newGame
    }

    suspend fun toggleTile(game: Game, row: Int, col: Int): Game = withContext(Dispatchers.IO) {
        val updatedGrid = game.grid.mapIndexed { r, gridRow ->
            gridRow.mapIndexed { c, tile ->
                if (r == row && c == col) tile.copy(isChecked = !tile.isChecked) else tile
            }
        }

        val isWon = checkWin(updatedGrid, game.matrixSize)
        val updatedGame = game.copy(grid = updatedGrid, isWon = isWon)

        dao.insertGame(
            GameEntity(
                updatedGame.id,
                updatedGame.presetId,
                updatedGame.presetName,
                updatedGame.matrixSize,
                updatedGame.createdAt,
                updatedGame.grid,
                updatedGame.isWon,
            )
        )
        updatedGame
    }

    private fun checkWin(grid: List<List<Tile>>, size: Int): Boolean {
        if (grid.any { row -> row.all { it.isChecked } }) return true
        for (col in 0 until size) {
            if ((0 until size).all { r -> grid[r][col].isChecked }) return true
        }
        if ((0 until size).all { i -> grid[i][i].isChecked }) return true
        if ((0 until size).all { i -> grid[i][size - 1 - i].isChecked }) return true
        return false
    }
}