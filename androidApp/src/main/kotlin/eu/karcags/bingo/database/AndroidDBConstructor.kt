package eu.karcags.bingo.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

class AndroidDBConstructor(private val context: Context) : DBConstructor {
    override fun initialize(): BingoDatabase {
        val dbFile = context.getDatabasePath("bingo_application_store.db")
        return Room.databaseBuilder<BingoDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}