package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.repository.BingoRepository

@Composable
fun PresetListScreen(
    repository: BingoRepository,
    onGameCreated: (Game) -> Unit,
) {
    var presets by remember { mutableStateOf(repository.getPresets()) }
    var jsonInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            "Available Layouts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(presets) { preset ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                preset.name,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "${preset.matrixSize} x ${preset.matrixSize} Grid",
                            )
                        }
                        Button(
                            onClick = {
                                onGameCreated(repository.createGame(preset))
                            }
                        ) {
                            Text("Generate")
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = jsonInput,
            onValueChange = { jsonInput = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            placeholder = {
                Text("Paste JSON preset code here...")
            }
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
            )
        }

        Button(
            onClick = {
                val res = repository.importPresetFromJson(jsonInput)
                if (res.isSuccess) {
                    presets = repository.getPresets()
                    jsonInput = ""
                    errorMessage = ""
                } else {
                    errorMessage = "Import Failied: ${res.exceptionOrNull()?.message}"
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp),
        ) {
            Text("Import Preset")
        }
    }
}