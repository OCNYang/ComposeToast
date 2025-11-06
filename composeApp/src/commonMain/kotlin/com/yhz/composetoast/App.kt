package com.yhz.composetoast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun App() {
    MaterialTheme {
        ProvideToastManager(toastContent = null) { // Can customize global Toast layout here
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
                    Toast.show(
                        "This's short Toast!",
                        backgroundColor = Color(0xe0000000),
                        textColor = Color.White
                    )
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

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Custom Toast Layout Examples",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            val customToastManager = viewModel<ToastManager>()

            ToastHost(
                toastManager = customToastManager,
                toastContent = { toastData, maxWidth, onDismiss ->
                    CustomToastContent(toastData, maxWidth, onDismiss)
                }
            ) {
                Button(
                    onClick = {
                        customToastManager.showToast(
                            message = "I'm a Custom Toast",
                            backgroundColor = Color.White,
                            imageVector = ToastIcons.Success,
                            actions = listOf(
                                ActionData(label = "cancel", onAction = {}),
                                ActionData(label = "submit", onAction = {}),
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text("Show Toast (with Custom Layout)")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

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
                // Use DialogToastContent wrapper to automatically handle cross-platform Toast display
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

@Composable
private fun CustomToastContent(
    toast: ToastData,
    maxWidth: Dp,
    onDismiss: () -> Unit,
) {
    val backgroundColor = toast.backgroundColor ?: MaterialTheme.colorScheme.surfaceVariant
    val textColor = toast.textColor ?: MaterialTheme.colorScheme.onSurfaceVariant
    val iconColor = toast.iconColor ?: MaterialTheme.colorScheme.primary
    val actionColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = maxWidth)
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            toast.imageVector?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = "Toast icon",
                    tint = iconColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = toast.message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                toast.actions.forEach { action ->
                    TextButton(
                        onClick = {
                            action.onAction()
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = action.label,
                            color = action.actionColor ?: actionColor
                        )
                    }
                }
            }
        }
    }
}
