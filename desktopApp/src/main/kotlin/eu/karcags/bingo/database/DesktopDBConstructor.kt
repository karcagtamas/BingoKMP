package eu.karcags.bingo.database

import androidx.room.Room
import java.io.File
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

class DesktopDBConstructor : DBConstructor {
    override fun initialize(): BingoDatabase {
        val homeDir = System.getProperty("user.home")
        val appDir = File(homeDir, ".kmp_bingo_game")
        if (!appDir.exists()) appDir.mkdirs()

        val dbFile = File(appDir, "bingo_application_store.db")
        return Room.databaseBuilder<BingoDatabase>(
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}