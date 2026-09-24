package eu.karcags.bingo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.karcags.bingo.model.FixedTile
import eu.karcags.bingo.model.Preset
import eu.karcags.bingo.repository.BingoRepository
import kotlinx.coroutines.launch
import kotlin.time.Clock

@Composable
fun PresetEditorScreen(
    editingPreset: Preset?,
    repository: BingoRepository,
    onSaveSuccess: () -> Unit,
    onCancel: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var presetName by remember { mutableStateOf(editingPreset?.name ?: "") }
    var matrixSize by remember { mutableStateOf(editingPreset?.matrixSize ?: 5) }
    var currentItemText by remember { mutableStateOf("") }
    var poolItems by remember { mutableStateOf(editingPreset?.poolTiles ?: emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = if (editingPreset == null) "🆕 Create New Bingo Preset" else "✏️ Edit Bingo Preset",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp),
        )

        OutlinedTextField(
            value = presetName,
            onValueChange = { presetName = it },
            label = { Text("Preset Title Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Grid Matrix Size: ${matrixSize}x${matrixSize}", fontSize = 16.sp)
            Slider(
                value = matrixSize.toFloat(),
                onValueChange = { matrixSize = it.toInt() },
                valueRange = 3f..7f,
                steps = 3,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 12.dp),
        )

        Text(
            text = "Pool Items Configuration (${poolItems.size} added)",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
        Text(
            text = "Required random pool slots for a clean layout match: ${(matrixSize * matrixSize) - 1}",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(bottom = 8.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            OutlinedTextField(
                value = currentItemText,
                onValueChange = { currentItemText = it },
                placeholder = { Text("Type item phrase here...") },
                modifier = Modifier
                    .weight(1f),
                maxLines = 2,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (currentItemText.isNotBlank()) {
                        poolItems = poolItems + currentItemText.trim()
                        currentItemText = ""
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            ) {
                Text("Add")
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            itemsIndexed(poolItems) { idx, phrase ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("${idx + 1}. $phrase", modifier = Modifier.weight(1f), fontSize = 14.sp)
                        TextButton(
                            onClick = {
                                poolItems = poolItems.filterIndexed { i, _ -> i != idx }
                            },
                        ) {
                            Text("Remove", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(vertical = 4.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    val validation = repository.validatePreset(presetName, matrixSize, poolItems)
                    if (validation.isSuccess) {
                        scope.launch {
                            val centerCoordinate = matrixSize / 2
                            val generatedPreset = Preset(
                                id = editingPreset?.id ?: "custom_preset_${Clock.System.now().toEpochMilliseconds()}",
                                name = presetName.trim(),
                                matrixSize = matrixSize,
                                fixedTiles = listOf(
                                    FixedTile(
                                        centerCoordinate,
                                        centerCoordinate,
                                        "FREE SPACE",
                                        isPermanentlyToggled = true
                                    )
                                ),
                                poolTiles = poolItems.filter { it.isNotBlank() },
                            )
                            repository.savePreset(generatedPreset)
                            onSaveSuccess()
                        }
                    }
                },
            ) {
                Text("Save Preset Layout")
            }
        }
    }
}