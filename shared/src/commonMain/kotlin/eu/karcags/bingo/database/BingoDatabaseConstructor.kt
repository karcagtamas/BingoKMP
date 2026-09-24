package eu.karcags.bingo.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object BingoDatabaseConstructor : RoomDatabaseConstructor<BingoDatabase> {
    override fun initialize(): BingoDatabase
}