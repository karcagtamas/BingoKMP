package eu.karcags.bingo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.karcags.bingo.model.FixedTile

@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val matrixSize: Int,
    val fixedTiles: List<FixedTile>,
    val poolTiles: List<String>,
)