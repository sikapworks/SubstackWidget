package uk.ac.tees.mad.substackwidget.presentation.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.substackwidget.domain.model.Publication

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel,
    onDone: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Your publications", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Add every Substack you want grouped into this widget.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.inputText,
                onValueChange = viewModel::onInputChange,
                label = { Text("e.g. androidengineers") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = viewModel::addPublication,
                enabled = !state.isChecking
            ) {
                Text(if (state.isChecking) "..." else "Add")
            }
        }

        state.errorMessage?.let {
            Spacer(Modifier.height(4.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoadingSaved) {
            Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.publications.isEmpty()) {
            Text(
                "No publications yet — add one above.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.publications, key = { it.handle }) { publication ->
                    PublicationRow(
                        publication = publication,
                        onRemove = { viewModel.removePublication(publication.handle) }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onDone,
            enabled = state.publications.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Widget")
        }
    }
}

@Composable
private fun PublicationRow(publication: Publication, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(publication.displayName, style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Close, contentDescription = "Remove ${publication.displayName}")
        }
    }
}