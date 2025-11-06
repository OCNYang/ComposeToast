# Quick Start Guide - ComposeToast

## 快速开始

### 1. 运行示例应用

```bash
# Android
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run

# Web
./gradlew :composeApp:jsBrowserDevelopmentRun
```

### 2. 在你的项目中使用

#### 步骤 1: 添加 ToastHost

在你的根 Composable 中包裹 ToastHost：

```kotlin
import com.yhz.composetoast.*

@Composable
fun App() {
    val toastManager = remember { ToastManager() }

    MaterialTheme {
        ToastHost(toastManager = toastManager) {
            // 你的应用内容
            MainScreen(toastManager = toastManager)
        }
    }
}
```

#### 步骤 2: 显示 Toast

```kotlin
@Composable
fun MainScreen(toastManager: ToastManager) {
    Button(onClick = {
        toastManager.showSuccess("Hello, Toast!")
    }) {
        Text("Show Toast")
    }
}
```

### 3. 常用示例

```kotlin
// 信息提示
toastManager.showInfo("This is an info message")

// 成功提示
toastManager.showSuccess("Operation completed!")

// 警告提示
toastManager.showWarning("Please check your input")

// 错误提示
toastManager.showError("Something went wrong")

// 带操作按钮
toastManager.showToast(
    message = "Item deleted",
    actionLabel = "Undo",
    onAction = { /* 撤销操作 */ }
)

// 自定义位置和时长
toastManager.showToast(
    message = "Custom toast",
    position = ToastPosition.TOP,
    duration = 5000L  // 5秒
)
```

### 4. API 速查

| 方法 | 说明 | 默认时长 |
|------|------|----------|
| `showInfo(message)` | 信息提示 | 3秒 |
| `showSuccess(message)` | 成功提示 | 3秒 |
| `showWarning(message)` | 警告提示 | 3.5秒 |
| `showError(message)` | 错误提示 | 4秒 |
| `showToast(...)` | 自定义 Toast | 自定义 |
| `dismissCurrent()` | 关闭当前 Toast | - |
| `clear()` | 清除所有 Toast | - |

## 完整文档

详细文档请查看: [toast/README.md](toast/README.md)

## 项目结构

```
ComposeToast/
├── toast/                    # Toast 库模块
│   ├── src/
│   │   ├── commonMain/       # 跨平台核心代码
│   │   ├── androidMain/      # Android 平台实现
│   │   ├── iosMain/          # iOS 平台实现
│   │   └── commonTest/       # 单元测试
│   ├── build.gradle.kts      # 模块配置
│   └── README.md             # 详细文档
└── composeApp/               # 示例应用
    └── src/commonMain/
        └── App.kt            # Toast 演示
```

## 支持的平台

✅ Android (API 24+)
✅ iOS (14.0+)
✅ JVM Desktop
✅ Web (JS/WASM)

## 获取帮助

- 查看示例代码: `composeApp/src/commonMain/kotlin/com/yhz/composetoast/App.kt`
- 阅读完整文档: `toast/README.md`
- 查看实现总结: `IMPLEMENTATION_SUMMARY.md`
