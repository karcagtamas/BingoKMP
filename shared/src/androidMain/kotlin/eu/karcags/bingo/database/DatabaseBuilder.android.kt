package eu.karcags.bingo.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

lateinit var databaseContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<BingoDatabase> {
    val dbFile = databaseContext.getDatabasePath("bingo_application_store.db")
    return Room.databaseBuilder<BingoDatabase>(
        context = databaseContext,
        name = dbFile.absolutePath,
    )
}