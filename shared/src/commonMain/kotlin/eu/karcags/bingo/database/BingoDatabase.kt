package eu.karcags.bingo.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
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

@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val matrixSize: Int,
    val fixedTiles: List<FixedTile>,
    val poolTiles: List<String>,
)

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

@Dao
interface BingoDao {
    @Query("SELECT * FROM presets")
    suspend fun getAllPresets(): List<PresetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: PresetEntity)

    @Query("SELECT * FROM games ORDER BY createdAt DESC")
    suspend fun getAllGames(): List<GameEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity)
}

@Database(entities = [PresetEntity::class, GameEntity::class], version = 1)
@TypeConverters(BingoConverters::class)
abstract class BingoDatabase : RoomDatabase() {
    abstract fun bingoDao(): BingoDao
}

interface DBConstructor {
    fun initialize(): BingoDatabase
}