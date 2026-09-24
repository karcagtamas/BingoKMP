package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.Game
import eu.karcags.bingo.model.Preset
import eu.karcags.bingo.repository.BingoRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun PresetListScreen(
    repository: BingoRepository,
    onGameCreated: (Game) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var presets by remember { mutableStateOf<List<Preset>>(emptyList()) }
    var jsonInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    var exportedPayload by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        presets = repository.getPresets()
    }

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
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = preset.name,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "${preset.matrixSize} x ${preset.matrixSize} Grid",
                            )
                        }
                        Row {
                            TextButton(
                                onClick = {
                                    exportedPayload = repository.exportPresetToJson(preset)
                                },
                            ) {
                                Text("Export")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        onGameCreated(repository.createGameFromPreset(preset))
                                    }
                                },
                            ) {
                                Text("Generate")
                            }
                        }
                    }
                }
            }
        }

        if (exportedPayload.isNotEmpty()) {
            OutlinedTextField(
                value = exportedPayload,
                onValueChange = {},
                readOnly = true,
                label = { Text("Exported JSON (Copy This Block)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary),
            )

            Button(
                onClick = {
                    exportedPayload = ""
                },
                modifier = Modifier
                    .align(Alignment.End),
            ) {
                Text("Dismiss")
            }
            Spacer(modifier = Modifier.height(8.dp))
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
                scope.launch {
                    try {
                        val parsedPreset = Json.decodeFromString<Preset>(jsonInput)
                        repository.savePreset(parsedPreset)
                        presets = repository.getPresets()
                        jsonInput = ""
                        errorMessage = ""
                    } catch (e: Exception) {
                        errorMessage = "Import Failed: ${e.message}"
                    }
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