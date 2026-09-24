package eu.karcags.bingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import eu.karcags.bingo.database.createBingoDatabase
import eu.karcags.bingo.database.databaseContext
import eu.karcags.bingo.repository.PersistentBingoRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseContext = applicationContext

        val database = createBingoDatabase()
        val repository = PersistentBingoRepository(database.bingoDao())

        setContent {
            App(repository)
        }
    }
}