package eu.karcags.bingo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.karcags.bingo.model.Tile

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val id: String,
    val presetId: String,
    val presetName: String,
    val matrixSize: Int,
    val createdAt: Long,
    val grid: List<List<Tile>>,
    val isWon: Boolean,
)