package com.example.aroundegyptapplication.presentation.home.view.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerExperienceCard(
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false
) {
    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    Column(
        modifier = modifier.then(
            if (!isHorizontal) Modifier.fillMaxWidth() else Modifier
        )
    ) {
        // Image Card Shimmer
        Box(
            modifier = Modifier
                .then(if (isHorizontal) Modifier.width(336.dp) else Modifier.fillMaxWidth())
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(brush)
        )

        // Title and Like Section Shimmer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title Shimmer
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .background(
                        brush = brush,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            // Like Section Shimmer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(20.dp)
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(4.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    Box(
        modifier = modifier.background(
            brush = brush,
            shape = RoundedCornerShape(4.dp)
        )
    )
}