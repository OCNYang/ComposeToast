package com.yhz.composetoast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        ProvideToastManager {
            ToastDemoScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToastDemoScreen() {
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


            // Long Toast
            Button(
                onClick = {
                    Toast.show("This's short Toast!")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Show text Toast")
            }

            // Long Toast
            Button(
                onClick = {
                    Toast.show("Show Long long long long long long long long long long long long long long Toast!")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Show Long-text Toast")
            }

            // Info Toast
            Button(
                onClick = {
                    Toast.showInfo("This is an informational message")
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
                    Toast.showSuccess("Operation completed successfully!")
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
                    Toast.showWarning("Please check your input carefully")
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
                    Toast.showError("An error occurred while processing!")
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
                    Toast.show(
                        message = "Item deleted from your cart",
                        actions = arrayOf(
                            ActionData(
                                "Undo",
                                onAction = {
                                    Toast.showSuccess("Action undone!")
                                }
                            )
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Toast with Action Button")
            }

            // Top Position Toast
            Button(
                onClick = {
                    Toast.show(
                        message = "This toast appears at the top",
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
                    Toast.show(
                        message = "This toast appears in the center",
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
                    Toast.show(
                        message = "This toast stays for 6 seconds",
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
                        Toast.showInfo("Toast message #${index + 1}")
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
                    Toast.clear()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear All Toasts")
            }
        }
    }

    // Dialog
    WithToastComposable(showDialog) { toastManager ->
        AlertDialog(
            modifier = Modifier.fillMaxHeight(0.4f),
            onDismissRequest = { showDialog = false },
            title = {
                Text("Dialog Example")
            },
            text = {
                // 使用 dialogToastContent 包装，自动处理跨平台 Toast 显示
                DialogToastContent(toastManager = toastManager) {
                    Text("Click the button below to show a toast from inside this dialog!")
                }
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
