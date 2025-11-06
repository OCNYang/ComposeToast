# Compose Toast Library

一个基于 Compose Multiplatform 的跨平台 Toast 组件库，使用 Overlay 方案实现，支持 Android、iOS、JVM Desktop、Web (JS)、WASM。

## 特性

✅ 跨平台支持（Android、iOS、Desktop、Web）
✅ 不阻挡用户交互（Overlay 方案）
✅ 自动队列管理
✅ 4 种预设类型（Info、Success、Warning、Error）
✅ 自定义位置（Top、Center、Bottom）
✅ 自定义显示时长
✅ 支持操作按钮
✅ 流畅的进入/退出动画
✅ Material 3 设计

## 使用方法

### 1. 添加依赖

在你的 `build.gradle.kts` 中：

```kotlin
dependencies {
    implementation(project(":toast"))
}
```

### 2. 基本使用

```kotlin
import com.yhz.composetoast.*

@Composable
fun App() {
    val toastManager = remember { ToastManager() }

    MaterialTheme {
        // 使用 ToastHost 包裹你的内容
        ToastHost(toastManager = toastManager) {
            YourScreen(toastManager = toastManager)
        }
    }
}

@Composable
fun YourScreen(toastManager: ToastManager) {
    Column {
        Button(onClick = {
            toastManager.showInfo("Hello, Toast!")
        }) {
            Text("Show Toast")
        }
    }
}
```

### 3. 预设类型

```kotlin
// 信息提示
toastManager.showInfo("This is an info message")

// 成功提示
toastManager.showSuccess("Operation completed!")

// 警告提示
toastManager.showWarning("Please check your input")

// 错误提示
toastManager.showError("Something went wrong!")
```

### 4. 自定义 Toast

```kotlin
toastManager.showToast(
    message = "Custom toast",
    type = ToastType.INFO,
    duration = 5000L,  // 5 秒
    position = ToastPosition.TOP,
    actionLabel = "Undo",
    onAction = {
        // 处理操作按钮点击
        println("Action clicked")
    }
)
```

### 5. 位置选项

```kotlin
// 顶部显示
toastManager.showToast(
    message = "Top toast",
    position = ToastPosition.TOP
)

// 中间显示
toastManager.showToast(
    message = "Center toast",
    position = ToastPosition.CENTER
)

// 底部显示（默认）
toastManager.showToast(
    message = "Bottom toast",
    position = ToastPosition.BOTTOM
)
```

### 6. 带操作按钮

```kotlin
toastManager.showToast(
    message = "Item deleted",
    type = ToastType.INFO,
    actionLabel = "Undo",
    onAction = {
        // 撤销操作
        restoreItem()
    }
)
```

### 7. 队列管理

```kotlin
// 连续显示多个 Toast，会自动排队
repeat(5) { index ->
    toastManager.showInfo("Message #${index + 1}")
}

// 清除所有 Toast
toastManager.clear()

// 关闭当前 Toast（会自动显示下一个）
toastManager.dismissCurrent()
```

## API 参考

### ToastManager

| 方法 | 说明 |
|------|------|
| `showToast(...)` | 显示自定义 Toast |
| `showInfo(message, duration)` | 显示信息 Toast |
| `showSuccess(message, duration)` | 显示成功 Toast |
| `showWarning(message, duration)` | 显示警告 Toast |
| `showError(message, duration)` | 显示错误 Toast |
| `dismissCurrent()` | 关闭当前 Toast |
| `clear()` | 清除所有 Toast |

### ToastData

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `message` | String | - | Toast 消息内容 |
| `type` | ToastType | INFO | Toast 类型 |
| `duration` | Long | 3000L | 显示时长（毫秒） |
| `position` | ToastPosition | BOTTOM | 显示位置 |
| `actionLabel` | String? | null | 操作按钮文本 |
| `onAction` | (() -> Unit)? | null | 操作按钮回调 |

### ToastType

- `INFO` - 信息（蓝色）
- `SUCCESS` - 成功（绿色）
- `WARNING` - 警告（橙色）
- `ERROR` - 错误（红色）

### ToastPosition

- `TOP` - 顶部
- `CENTER` - 中间
- `BOTTOM` - 底部（默认）

## 在 ViewModel 中使用

```kotlin
class MyViewModel(
    private val toastManager: ToastManager
) : ViewModel() {

    fun performAction() {
        viewModelScope.launch {
            try {
                // 执行操作
                repository.save(data)
                toastManager.showSuccess("Data saved successfully")
            } catch (e: Exception) {
                toastManager.showError("Failed to save: ${e.message}")
            }
        }
    }
}

// 使用
@Composable
fun MyScreen(
    toastManager: ToastManager,
    viewModel: MyViewModel = viewModel { MyViewModel(toastManager) }
) {
    // ...
}
```

## 完整示例

运行 `composeApp` 模块查看完整的示例演示，包括：

- 基本 Toast 类型
- 不同位置
- 带操作按钮
- 队列管理
- 自定义时长

## 技术实现

### Overlay 方案

本库采用 Overlay 方案而非 Dialog 方案，主要优势：

1. **不阻挡用户交互** - Toast 下方的内容仍然可以点击
2. **性能更好** - 无 Dialog 的额外开销
3. **更接近原生体验** - 类似 Android 原生 Toast

### 跨平台支持

- **Android**: 完美支持
- **iOS**: 完美支持
- **JVM Desktop**: 完美支持
- **Web (JS/WASM)**: 完美支持

## 许可证

MIT License
