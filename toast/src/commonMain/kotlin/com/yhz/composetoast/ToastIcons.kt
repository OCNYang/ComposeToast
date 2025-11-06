package com.yhz.composetoast

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Toast icon collection
 *
 * Provides vector icons for different Toast types
 */
object ToastIcons {

    /**
     * Success icon (checkmark in circle)
     */
    val Success: ImageVector
        get() {
            if (_success != null) {
                return _success!!
            }
            _success = ImageVector.Builder(
                name = "Success",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 1024f,
                viewportHeight = 1024f
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF666666)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    // 外圆
                    moveTo(510.65f, 959.35f)
                    curveToRelative(-247.26f, 0f, -447.69f, -200.45f, -447.69f, -447.7f)
                    curveToRelative(0f, -247.25f, 200.44f, -447.69f, 447.69f, -447.69f)
                    curveToRelative(247.26f, 0f, 447.69f, 200.45f, 447.69f, 447.69f)
                    curveToRelative(0f, 247.25f, -200.44f, 447.7f, -447.69f, 447.7f)
                    close()

                    // 对勾
                    moveTo(784.19f, 354.24f)
                    lineToRelative(-14.39f, -14.39f)
                    curveToRelative(-7.95f, -7.95f, -20.83f, -7.95f, -28.78f, 0f)
                    lineTo(442.6f, 632.09f)
                    lineTo(333.04f, 517.43f)
                    curveToRelative(-3.6f, -3.6f, -12.96f, -0.06f, -20.91f, 7.89f)
                    lineToRelative(-14.39f, 14.39f)
                    curveToRelative(-7.95f, 7.95f, -11.47f, 17.31f, -7.87f, 20.9f)
                    lineToRelative(146.19f, 152.97f)
                    curveToRelative(3.58f, 3.6f, 12.94f, 0.05f, 20.89f, -7.89f)
                    lineToRelative(14.37f, -14.39f)
                    curveToRelative(2.54f, -2.52f, 4.41f, -5.14f, 5.96f, -7.71f)
                    lineToRelative(306.91f, -300.56f)
                    curveToRelative(7.95f, -7.95f, 7.95f, -20.85f, 0f, -28.8f)
                    close()
                }
            }.build()
            return _success!!
        }
    private var _success: ImageVector? = null

    /**
     * Info icon (i in circle)
     */
    val Info: ImageVector
        get() {
            if (_info != null) {
                return _info!!
            }
            _info = ImageVector.Builder(
                name = "Info",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 1024f,
                viewportHeight = 1024f
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF272636)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    // 外圆
                    moveTo(512f, 956.8f)
                    curveToRelative(245.89f, 0f, 445.95f, -200.06f, 445.95f, -445.95f)
                    curveToRelative(0f, -245.9f, -200.06f, -445.95f, -445.95f, -445.95f)
                    reflectiveCurveToRelative(-445.95f, 200.05f, -445.95f, 445.95f)
                    curveToRelative(0f, 245.89f, 200.06f, 445.95f, 445.95f, 445.95f)
                    close()

                    // 上方的点
                    moveTo(512f, 287.87f)
                    curveToRelative(30.78f, 0f, 55.74f, 24.96f, 55.74f, 55.75f)
                    curveToRelative(0f, 30.78f, -24.96f, 55.74f, -55.74f, 55.74f)
                    reflectiveCurveToRelative(-55.74f, -24.96f, -55.74f, -55.74f)
                    curveToRelative(0f, -30.79f, 24.96f, -55.75f, 55.74f, -55.75f)
                    close()

                    // 下方的竖线
                    moveTo(456.26f, 510.85f)
                    curveToRelative(0f, -30.78f, 24.96f, -55.74f, 55.74f, -55.74f)
                    reflectiveCurveToRelative(55.74f, 24.96f, 55.74f, 55.74f)
                    verticalLineToRelative(167.23f)
                    curveToRelative(0f, 30.78f, -24.96f, 55.74f, -55.74f, 55.74f)
                    reflectiveCurveToRelative(-55.74f, -24.96f, -55.74f, -55.74f)
                    verticalLineToRelative(-167.23f)
                    close()
                }
            }.build()
            return _info!!
        }
    private var _info: ImageVector? = null

    /**
     * Error icon (X in circle)
     */
    val Error: ImageVector
        get() {
            if (_error != null) {
                return _error!!
            }
            _error = ImageVector.Builder(
                name = "Error",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 1024f,
                viewportHeight = 1024f
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    // 外圆
                    moveTo(512f, 62f)
                    curveTo(264.5f, 62f, 62f, 264.5f, 62f, 512f)
                    reflectiveCurveToRelative(202.5f, 450f, 450f, 450f)
                    reflectiveCurveToRelative(450f, -202.5f, 450f, -450f)
                    reflectiveCurveTo(759.5f, 62f, 512f, 62f)
                    close()

                    // 叉号路径
                    moveTo(708.88f, 708.88f)
                    curveToRelative(-11.25f, 11.25f, -33.75f, 11.25f, -45f, 0f)
                    lineTo(512f, 557f)
                    lineToRelative(-151.88f, 151.88f)
                    curveToRelative(-11.25f, 11.25f, -33.75f, 11.25f, -45f, 0f)
                    curveToRelative(-11.25f, -11.25f, -11.25f, -33.75f, 0f, -45f)
                    lineTo(467f, 512f)
                    lineTo(315.12f, 360.12f)
                    curveToRelative(-11.25f, -16.88f, -16.88f, -33.75f, 0f, -45f)
                    curveToRelative(11.25f, -11.25f, 33.75f, -11.25f, 45f, 0f)
                    lineTo(512f, 467f)
                    lineToRelative(151.88f, -151.88f)
                    curveToRelative(11.25f, -11.25f, 33.75f, -11.25f, 45f, 0f)
                    curveToRelative(11.25f, 11.25f, 11.25f, 33.75f, 0f, 45f)
                    lineTo(557f, 512f)
                    lineToRelative(151.88f, 151.88f)
                    curveToRelative(11.25f, 16.88f, 16.88f, 33.75f, 0f, 45f)
                    close()
                }
            }.build()
            return _error!!
        }
    private var _error: ImageVector? = null

    /**
     * Warning icon (exclamation mark in circle)
     */
    val Warning: ImageVector
        get() {
            if (_warning != null) {
                return _warning!!
            }
            _warning = ImageVector.Builder(
                name = "Warning",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 1024f,
                viewportHeight = 1024f
            ).apply {
                path(
                    fill = SolidColor(Color(0xFF000000)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.NonZero
                ) {
                    // 外圆
                    moveTo(512f, 962f)
                    arcToRelative(450.51f, 450.51f, 0f, false, true, -450f, -450f)
                    arcToRelative(450.51f, 450.51f, 0f, false, true, 450f, -450f)
                    arcToRelative(450.51f, 450.51f, 0f, false, true, 450f, 450f)
                    arcToRelative(450.51f, 450.51f, 0f, false, true, -450f, 450f)
                    close()

                    // 下方的点
                    moveTo(512f, 680.75f)
                    arcToRelative(49.28f, 49.28f, 0f, false, false, -49.22f, 49.22f)
                    arcToRelative(49.28f, 49.28f, 0f, false, false, 49.22f, 49.22f)
                    arcToRelative(49.28f, 49.28f, 0f, false, false, 49.22f, -49.22f)
                    arcTo(49.28f, 49.28f, 0f, false, false, 512f, 680.75f)
                    close()

                    // 上方的竖线
                    moveTo(512f, 258.88f)
                    arcToRelative(42.24f, 42.24f, 0f, false, false, -42.19f, 42.19f)
                    verticalLineToRelative(253.12f)
                    arcTo(42.24f, 42.24f, 0f, false, false, 512f, 596.38f)
                    arcToRelative(42.24f, 42.24f, 0f, false, false, 42.24f, -42.19f)
                    verticalLineTo(301.06f)
                    arcTo(42.24f, 42.24f, 0f, false, false, 512f, 258.88f)
                    close()
                }
            }.build()
            return _warning!!
        }
    private var _warning: ImageVector? = null
}
