# Compose Toast

[![](https://jitpack.io/v/yourusername/ComposeToast.svg)](https://jitpack.io/#yourusername/ComposeToast)
![badge][badge-android]
![badge][badge-ios]
![badge][badge-js]
![badge][badge-wasm]
![badge][badge-jvm]

A beautiful, customizable Toast library for Kotlin Multiplatform Compose with support for Android, iOS, Desktop, Web, and WASM.

## Live Demo

**[Try the Web Demo](https://yourusername.github.io/ComposeToast/)**

Experience all the Toast features directly in your browser!

## Features

- ✅ **Kotlin Multiplatform** - Works on Android, iOS, Desktop, Web, and WASM
- 🎨 **Fully Customizable** - Custom colors, icons, and layouts
- 📍 **Multiple Positions** - Top, Center, Bottom
- ⚡ **Action Buttons** - Add interactive action buttons to your toasts
- 🔄 **Queue Management** - Automatic toast queue with smooth transitions
- 🎭 **Built-in Types** - Success, Error, Warning, Info with default styling
- 🪟 **Dialog Support** - Works correctly inside Dialogs across all platforms
- 🎯 **Type-Safe API** - Kotlin-first API with default parameters

## Installation

### Step 1: Add JitPack repository

Add it in your root `build.gradle.kts` at the end of repositories:

```kotlin
allprojects {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Or in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add the dependency

In your module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.github.yourusername:ComposeToast:1.0.0")
            }
        }
    }
}
```

Replace `yourusername` with your actual GitHub username and `1.0.0` with the desired version or tag.

## Quick Start

### 1. Setup ToastManager at your app root

```kotlin
@Composable
fun App() {
    MaterialTheme {
        ProvideToastManager {
            YourAppContent()
        }
    }
}
```

### 2. Show Toasts from anywhere

```kotlin
// Success Toast
Toast.showSuccess("Operation completed!")

// Error Toast
Toast.showError("Something went wrong!")

// Warning Toast
Toast.showWarning("Please check your input")

// Info Toast
Toast.showInfo("Processing your request...")

// Custom Toast
Toast.show(
    message = "Custom message",
    imageVector = Icons.Default.Star,
    backgroundColor = Color.Blue,
    textColor = Color.White,
    duration = 3000L,
    position = ToastPosition.TOP
)
```

## Usage Examples

### Toast with Action Button

```kotlin
Toast.show(
    message = "Item deleted from cart",
    actions = arrayOf(
        ActionData("Undo") {
            Toast.showSuccess("Action undone!")
        }
    )
)
```

### Custom Position

```kotlin
// Top position
Toast.show("Message at top", position = ToastPosition.TOP)

// Center position
Toast.show("Message at center", position = ToastPosition.CENTER)

// Bottom position (default)
Toast.show("Message at bottom", position = ToastPosition.BOTTOM)
```

### Custom Duration

```kotlin
Toast.show(
    message = "This stays for 6 seconds",
    duration = 6000L
)
```

### In Dialogs

For proper Toast display in Dialogs (especially on Android/iOS):

```kotlin
var showDialog by remember { mutableStateOf(false) }

WithToastComposable(show = showDialog) { toastManager ->
    AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("Dialog Example") },
        text = {
            DialogToastContent(toastManager = toastManager) {
                Text("Click the button to show a toast!")
            }
        },
        confirmButton = {
            Button(onClick = {
                toastManager.showSuccess("Toast from Dialog!")
            }) {
                Text("Show Toast")
            }
        }
    )
}
```

### Custom Toast Layout

You can provide a completely custom Toast layout:

```kotlin
ProvideToastManager(
    toastContent = { toastData, maxWidth, onDismiss ->
        CustomToastContent(toastData, maxWidth, onDismiss)
    }
) {
    YourAppContent()
}
```

### Using ToastManager directly

```kotlin
@Composable
fun MyScreen() {
    val toastManager = rememberToastManager()

    Button(onClick = {
        toastManager.showSuccess("Clicked!")
    }) {
        Text("Click Me")
    }
}
```

## API Reference

### Toast (Global Object)

- `Toast.show()` - Show custom toast
- `Toast.showSuccess()` - Show success toast
- `Toast.showError()` - Show error toast
- `Toast.showWarning()` - Show warning toast
- `Toast.showInfo()` - Show info toast
- `Toast.clear()` - Clear all toasts

### ToastManager (Instance)

Use when you need more control or working with Dialogs:

- `toastManager.showToast()` - Show custom toast
- `toastManager.showSuccess()` - Show success toast
- `toastManager.showError()` - Show error toast
- `toastManager.showWarning()` - Show warning toast
- `toastManager.showInfo()` - Show info toast
- `toastManager.dismissCurrent()` - Dismiss current toast
- `toastManager.clear()` - Clear all toasts

### Components

- `ProvideToastManager` - Setup toast manager at app root
- `WithToastComposable` - Provide isolated toast manager for dialogs
- `DialogToastContent` - Wrapper for dialog content with toast support
- `rememberToastManager` - Get current toast manager instance

## Platform Support

| Platform | Support |
|----------|---------|
| Android  | ✅      |
| iOS      | ✅      |
| Desktop  | ✅      |
| Web (JS) | ✅      |
| WASM     | ✅      |

## License

MIT License - see [LICENSE](LICENSE) file for details

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

[badge-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat
[badge-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat
[badge-js]: http://img.shields.io/badge/-js-F8DB5D.svg?style=flat
[badge-wasm]: https://img.shields.io/badge/-wasm-624FE8.svg?style=flat
[badge-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg?style=flat
