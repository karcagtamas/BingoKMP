package eu.karcags.bingo.model

import kotlinx.serialization.Serializable

@Serializable
data class Preset(
    val id: String,
    val name: String,
    val matrixSize: Int,
    val fixedTiles: List<FixedTile> = emptyList(),
    val poolTiles: List<String> = emptyList(),
)

@Serializable
data class FixedTile(
    val row: Int,
    val col: Int,
    val label: String,
)

@Serializable
data class Game(
    val id: String,
    val presetId: String,
    val presetName: String,
    val matrixSize: Int,
    val createdAt: Long,
    val grid: List<List<Tile>>,
    val isWon: Boolean = false,
)

@Serializable
data class Tile(
    val id: String,
    val label: String,
    val isChecked: Boolean = false,
    val isFixed: Boolean = false,
)