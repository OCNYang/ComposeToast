# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ComposeToast is a Kotlin Multiplatform Compose library providing customizable Toast notifications with support for Android, iOS, Desktop (JVM), Web (JS), and WASM platforms.

**Key Architecture Points:**
- **Library Module** (`toast/`): Core Toast implementation published to JitPack
- **Demo App** (`composeApp/`): Multi-platform demo application showcasing Toast features
- **Multiplatform Strategy**: Expect/actual pattern for platform-specific implementations (see `Platform.kt` and `TimeUtils.kt`)

## Common Development Commands

### Building

```bash
# Build entire project (all platforms)
./gradlew build

# Build only the toast library module
./gradlew :toast:build

# Build only the demo app
./gradlew :composeApp:build

# Clean build artifacts
./gradlew clean
```

### Running Demo App

```bash
# Android
./gradlew :composeApp:installDebug

# Desktop (JVM)
./gradlew :composeApp:run

# Web (JS) - Development server
./gradlew :composeApp:jsBrowserRun

# WASM - Development server
./gradlew :composeApp:wasmJsBrowserRun
```

### Testing

```bash
# Run all tests
./gradlew test

# Run common tests only
./gradlew :toast:cleanAllTests :toast:allTests

# Android instrumented tests
./gradlew :toast:connectedAndroidTest
```

### Publishing

```bash
# Build library for publishing (creates maven artifacts)
./gradlew :toast:publishToMavenLocal
```

## Architecture

### Core Components

1. **ToastManager** (`toast/src/commonMain/kotlin/com/yhz/composetoast/ToastManager.kt`)
   - ViewModel-based manager handling Toast queue and lifecycle
   - Manages display timing, auto-dismissal, and queue processing
   - Provides both instance methods and global `Toast` singleton

2. **ToastHost** (`toast/src/commonMain/kotlin/com/yhz/composetoast/ToastHost.kt`)
   - Composable that renders Toast using `Popup` for proper layering
   - Handles animations (slide + fade) based on position
   - Critical: Uses `Popup` to display above most UI elements while keeping non-Toast areas interactive

3. **ToastData** (`toast/src/commonMain/kotlin/com/yhz/composetoast/ToastData.kt`)
   - Data model for Toast configuration
   - Defines `ToastPosition` enum (TOP, CENTER, BOTTOM)
   - Contains `ToastDefaults` with predefined color schemes
   - `ActionData` for interactive Toast buttons

4. **DialogToastSupport** (`toast/src/commonMain/kotlin/com/yhz/composetoast/DialogToastSupport.kt`)
   - Platform-aware Toast handling for Dialogs
   - **Critical**: Android/iOS require `ToastHost` inside Dialog (Popup cannot render above AlertDialog on these platforms)
   - Desktop/Web/WASM can use global `Popup` above Dialogs
   - `WithToastComposable`: Lifecycle-aware ToastManager for temporary components

### Platform-Specific Implementation Pattern

The library uses expect/actual declarations for platform differences:

- **Platform.kt**: Platform identification (`needsDialogToastWrapper()` determines if Dialog needs special handling)
- **TimeUtils.kt**: Platform-specific timestamp implementation (`currentTimeMillis()`)

Located in:
- `commonMain/` - Shared declarations (expect)
- `androidMain/`, `iosMain/`, `desktopMain/`, `jsMain/`, `wasmJsMain/` - Platform implementations (actual)

### Key Design Decisions

1. **Global vs Instance Access**
   - `Toast.showSuccess()` - Global singleton for convenience
   - `rememberToastManager()` - Instance access for scoped control (e.g., in Dialogs)

2. **Dialog Handling**
   - Must use `WithToastComposable` for Dialog-scoped ToastManager
   - Must use `DialogToastContent` wrapper inside Dialog on Android/iOS
   - Automatically clears Toast queue when Dialog dismisses

3. **Queue Management**
   - Toasts automatically queue and display sequentially
   - Each Toast auto-dismisses after its duration
   - Can manually clear with `Toast.clear()` or `toastManager.clear()`

4. **Customization Points**
   - Global layout: `ProvideToastManager(toastContent = { ... })`
   - Per-instance layout: `ToastHost(toastContent = { ... })`
   - Colors, icons, duration, position all configurable per-Toast

## Project Structure

```
toast/                          # Library module
├── src/
│   ├── commonMain/            # Shared KMP code
│   │   └── kotlin/com/yhz/composetoast/
│   │       ├── ToastManager.kt      # Core manager + global Toast object
│   │       ├── ToastHost.kt         # Popup-based rendering
│   │       ├── ToastData.kt         # Data models
│   │       ├── ToastIcons.kt        # Built-in icon vectors
│   │       ├── DialogToastSupport.kt # Dialog helpers
│   │       └── Platform.kt          # expect declarations
│   ├── androidMain/           # Android implementations
│   ├── iosMain/              # iOS implementations
│   ├── desktopMain/          # JVM Desktop implementations
│   ├── jsMain/               # Web JS implementations
│   └── wasmJsMain/           # WASM implementations
└── build.gradle.kts          # Multi-platform targets config

composeApp/                    # Demo application
└── src/
    ├── commonMain/kotlin/com/yhz/composetoast/
    │   └── App.kt            # Comprehensive Toast demos
    ├── androidMain/          # Android app entry
    ├── iosMain/             # iOS app entry
    ├── jvmMain/             # Desktop app entry
    ├── webMain/             # Web app entry
    └── wasmJsMain/          # WASM app entry
```

## Publishing Configuration

- **Group ID**: `com.github.ocnyang` (configured in `toast/build.gradle.kts:146`)
- **Artifact ID**: `compose-toast`
- **Version**: Managed via Git tags for JitPack
- **Framework Name** (iOS): `toastKit` (configured in `toast/build.gradle.kts:37`)

When updating for publishing, modify:
1. Group/artifact IDs in `toast/build.gradle.kts` (lines 146, 152)
2. Version tag in Git
3. JitPack will auto-build from GitHub releases/tags

## Common Development Patterns

### Adding a New Platform

1. Add target to `toast/build.gradle.kts` in `kotlin { ... }` block
2. Create new source set `src/<platform>Main/kotlin/com/yhz/composetoast/`
3. Implement `actual` declarations for `Platform.kt` and `TimeUtils.kt`
4. Update `composeApp/build.gradle.kts` if demo support needed

### Adding New Toast Features

1. Update `ToastData.kt` with new properties
2. Modify `ToastContent()` in `ToastHost.kt` to render new features
3. Add convenience methods to `ToastManager` if needed
4. Update `Toast` singleton to expose new functionality
5. Add demo example to `composeApp/src/commonMain/kotlin/.../App.kt`

### Testing Across Platforms

Since this is a UI library, testing is primarily visual through the demo app:
1. Make changes to `toast/` module
2. Run demo app on target platform(s)
3. Verify behavior using demo buttons in `App.kt`
4. Unit tests in `commonTest/` for business logic only (UI requires manual testing)
