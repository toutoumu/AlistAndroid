package com.github.jing332.alistandroid.ui.widgets

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun LabelSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,

    showButton: Boolean = true,
    buttonSteps: Float = 0.01f,
    buttonLongSteps: Float = 0.1f,

    onValueRemove: (Boolean) -> Unit = {
        onValueChange(value - (if (it) buttonLongSteps else buttonSteps))
    },
    onValueAdd: (Boolean) -> Unit = {
        onValueChange(value + if (it) buttonLongSteps else buttonSteps)
    },

    a11yDescription: String = "",
    text: @Composable BoxScope.() -> Unit,
) {
    val view = LocalView.current
    ConstraintLayout(modifier) {
        val (textRef, sliderRef) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(textRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .wrapContentHeight()
        ) {
            text()
        }
        Row(Modifier.constrainAs(sliderRef) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(textRef.bottom, margin = (-12).dp)
        }) {
            if (showButton)
                LongClickIconButton(
                    onClick = {
                        Log.e("BUG大无语事件", value.toString())
                        onValueRemove(false)
                    },
                    onLongClick = {
                        Log.e("BUG大无语事件", value.toString())
                        onValueRemove(true)
                    },
                    enabled = value > valueRange.start,
                    modifier = Modifier
                        .semantics {
                            stateDescription = a11yDescription
                            contentDescription = a11yDescription
                        }
                ) {
                    Icon(Icons.Default.Remove, "remove")
                }
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        stateDescription = a11yDescription
                        contentDescription = a11yDescription
                    },
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = onValueChangeFinished
            )
            if (showButton)
                LongClickIconButton(
                    onClick = {
                        Log.e("BUG大无语事件", value.toString())
                        onValueAdd(false)
                    },
                    onLongClick = {
                        Log.e("BUG大无语事件", value.toString())
                        onValueAdd(true)
                    },
                    enabled = value < valueRange.endInclusive,
                    modifier = Modifier
                        .semantics {
                            stateDescription = a11yDescription
                            contentDescription = a11yDescription
                        }
                ) {
                    Icon(Icons.Default.Add, "add")
                }

        }
    }
}

@Preview
@Composable
fun PreviewSlider() {
    var value by remember { mutableFloatStateOf(0f) }
    val str = "语速: $value"
    LabelSlider(
        value = value,
        onValueChange = { value = it },
        valueRange = 0.1f..3.0f,
        a11yDescription = str,
        buttonSteps = 0.1f
    ) {
        Text(str)
    }
}