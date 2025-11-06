package com.yhz.composetoast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    val toastManager = remember { ToastManager() }

    MaterialTheme {
        ToastHost(
            toastManager = toastManager
        ) {
            ToastDemoScreen(toastManager = toastManager)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToastDemoScreen(toastManager: ToastManager) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Toast Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize().verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Toast Examples",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Info Toast
            Button(
                onClick = {
                    toastManager.showInfo("This is an informational message")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Show Info Toast")
            }

            // Success Toast
            Button(
                onClick = {
                    toastManager.showSuccess("Operation completed successfully!")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Show Success Toast")
            }

            // Warning Toast
            Button(
                onClick = {
                    toastManager.showWarning("Please check your input carefully")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Show Warning Toast")
            }

            // Error Toast
            Button(
                onClick = {
                    toastManager.showError("An error occurred while processing!")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Show Error Toast")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Advanced Examples",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Toast with Action
            Button(
                onClick = {
                    toastManager.showToast(
                        message = "Item deleted from your cart",
                        type = ToastType.INFO,
                        actionLabel = "Undo",
                        onAction = {
                            toastManager.showSuccess("Action undone!")
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Toast with Action Button")
            }

            // Top Position Toast
            Button(
                onClick = {
                    toastManager.showToast(
                        message = "This toast appears at the top",
                        type = ToastType.INFO,
                        position = ToastPosition.TOP
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Top Position Toast")
            }

            // Center Position Toast
            Button(
                onClick = {
                    toastManager.showToast(
                        message = "This toast appears in the center",
                        type = ToastType.WARNING,
                        position = ToastPosition.CENTER
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Center Position Toast")
            }

            // Long Duration Toast
            Button(
                onClick = {
                    toastManager.showToast(
                        message = "This toast stays for 6 seconds",
                        type = ToastType.INFO,
                        duration = 6000L
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Long Duration Toast (6s)")
            }

            // Multiple Toasts (Queue)
            Button(
                onClick = {
                    repeat(5) { index ->
                        toastManager.showInfo("Toast message #${index + 1}")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Show 5 Toasts (Queue Demo)")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Dialog Examples",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Show Dialog with Toast
            Button(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Show Dialog (with Toast inside)")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Clear All
            OutlinedButton(
                onClick = {
                    toastManager.clear()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear All Toasts")
            }
        }
    }

    // Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text("Dialog Example")
            },
            text = {
                Text("Click the button below to show a toast from inside this dialog!")
            },
            confirmButton = {
                Button(
                    onClick = {
                        toastManager.showSuccess("Toast shown from Dialog!")
                    }
                ) {
                    Text("Show Toast")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
