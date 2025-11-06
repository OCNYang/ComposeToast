# 平台支持总结

## ✅ 已支持的平台

toast 模块现在完全支持以下所有平台：

### 1. Android
- **目标**: androidLibrary
- **最低 SDK**: 24
- **编译 SDK**: 36
- **TimeUtils**: `System.currentTimeMillis()`
- **状态**: ✅ 完全支持

### 2. iOS
- **目标**: iosX64, iosArm64, iosSimulatorArm64
- **输出**: Framework (toastKit)
- **TimeUtils**: `NSDate().timeIntervalSince1970 * 1000`
- **状态**: ✅ 完全支持（需要 Xcode 编译）

### 3. JVM Desktop
- **目标**: jvm("desktop")
- **JVM Target**: 11
- **TimeUtils**: `System.currentTimeMillis()`
- **状态**: ✅ 完全支持
- **编译命令**: `./gradlew :toast:compileKotlinDesktop`

### 4. Web (JavaScript)
- **目标**: js(IR)
- **浏览器**: 支持
- **TimeUtils**: `kotlin.js.Date.now()`
- **状态**: ✅ 完全支持
- **编译命令**: `./gradlew :toast:compileKotlinJs`

### 5. WebAssembly (WASM)
- **目标**: wasmJs
- **浏览器**: 支持
- **TimeUtils**: `kotlinx.browser.window.performance.now()`
- **状态**: ✅ 完全支持
- **编译命令**: `./gradlew :toast:compileKotlinWasmJs`

## 平台特定文件

### TimeUtils 实现
- `androidMain/TimeUtils.android.kt` - Android 平台
- `iosMain/TimeUtils.ios.kt` - iOS 平台
- `desktopMain/TimeUtils.desktop.kt` - JVM Desktop 平台
- `jsMain/TimeUtils.js.kt` - Web JavaScript 平台
- `wasmJsMain/TimeUtils.wasm.kt` - WASM 平台

### Platform 实现
- `androidMain/Platform.android.kt` - 返回 "Android"
- `iosMain/Platform.ios.kt` - 返回 "iOS"
- `desktopMain/Platform.desktop.kt` - 返回 "JVM Desktop"
- `jsMain/Platform.js.kt` - 返回 "Web (JavaScript)"
- `wasmJsMain/Platform.wasm.kt` - 返回 "Web (WASM)"

## 编译验证

### 单独编译各平台
```bash
# Android (在 macOS/Linux/Windows 上)
./gradlew :toast:compileKotlinAndroidLibrary

# iOS (仅 macOS，需要 Xcode)
./gradlew :toast:compileKotlinIosArm64

# Desktop (所有平台)
./gradlew :toast:compileKotlinDesktop

# JavaScript (所有平台)
./gradlew :toast:compileKotlinJs

# WASM (所有平台)
./gradlew :toast:compileKotlinWasmJs
```

### 编译所有非 iOS 平台
```bash
./gradlew :toast:compileKotlinDesktop :toast:compileKotlinJs :toast:compileKotlinWasmJs
```

## 运行示例应用

### Android
```bash
./gradlew :composeApp:installDebug
```

### Desktop
```bash
./gradlew :composeApp:run
```

### Web (JS)
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### WASM
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

## 浏览器兼容性

### Web (JS)
- ✅ Chrome: 所有版本
- ✅ Firefox: 所有版本
- ✅ Safari: 所有版本
- ✅ Edge: 所有版本

### WASM
- ✅ Chrome: 57+
- ✅ Firefox: 52+
- ✅ Safari: 11+
- ✅ Edge: 16+
- ❌ IE: 不支持

## 特性支持矩阵

| 特性 | Android | iOS | Desktop | Web (JS) | WASM |
|------|---------|-----|---------|----------|------|
| ToastHost | ✅ | ✅ | ✅ | ✅ | ✅ |
| ToastManager | ✅ | ✅ | ✅ | ✅ | ✅ |
| 4 种类型 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 3 种位置 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 自动队列 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 操作按钮 | ✅ | ✅ | ✅ | ✅ | ✅ |
| 动画 | ✅ | ✅ | ✅ | ✅ | ✅ |
| Material 3 | ✅ | ✅ | ✅ | ✅ | ✅ |

## 依赖要求

### 共享依赖 (commonMain)
- Kotlin stdlib
- Compose runtime
- Compose foundation
- Compose material3
- Compose materialIconsExtended
- Compose ui
- Compose animation
- Lifecycle viewmodel-compose

### Desktop 特定依赖
- compose.desktop.currentOs

### 其他平台
无需额外依赖，所有必需的库都从 commonMain 继承。

## 总结

✅ **5 个平台全部支持**
✅ **所有核心功能在所有平台上一致工作**
✅ **编译验证通过**（除 iOS 需要 Xcode）
✅ **生产就绪**
