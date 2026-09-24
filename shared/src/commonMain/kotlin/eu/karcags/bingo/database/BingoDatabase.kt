package eu.karcags.bingo.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PresetEntity::class, GameEntity::class], version = 1)
@TypeConverters(BingoConverters::class)
@ConstructedBy(BingoDatabaseConstructor::class)
abstract class BingoDatabase : RoomDatabase() {
    abstract fun bingoDao(): BingoDao
}