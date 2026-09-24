package eu.karcags.bingo.database

import androidx.room.TypeConverter
import eu.karcags.bingo.model.FixedTile
import eu.karcags.bingo.model.Tile
import kotlinx.serialization.json.Json

class BingoConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromGameGrid(grid: List<List<Tile>>): String = json.encodeToString(grid)

    @TypeConverter
    fun toGameGrid(value: String): List<List<Tile>> = json.decodeFromString(value)

    @TypeConverter
    fun fromFixedTiles(tiles: List<FixedTile>): String = json.encodeToString(tiles)

    @TypeConverter
    fun toFixedTiles(value: String): List<FixedTile> = json.decodeFromString(value)

    @TypeConverter
    fun fromStringList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)
}