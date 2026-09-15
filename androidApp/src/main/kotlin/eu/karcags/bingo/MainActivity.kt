package eu.karcags.bingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.karcags.bingo.database.AndroidDBConstructor
import eu.karcags.bingo.repository.PersistentBingoRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val database = AndroidDBConstructor(applicationContext).initialize()
        val repository = PersistentBingoRepository(database.bingoDao())

        setContent {
            App(repository)
        }
    }
}