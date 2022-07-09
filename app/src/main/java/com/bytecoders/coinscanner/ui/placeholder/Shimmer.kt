package com.bytecoders.coinscanner.ui.placeholder

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun ShimmerPreview() {
    ShimmerGridItem(
        brush = Brush.linearGradient(
            listOf(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            )
        )
    )
}

@Composable
fun LoadingShimmerEffect() {
    val gradient = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f), // darker grey (90% opacity)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), // lighter grey (30% opacity)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition() // animate infinite times

    val translateAnimation = transition.animateFloat( // animate the transition
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // duration for the animation
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    ShimmerGridItem(brush = brush)
}

@Composable
fun ShimmerGridItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalAlignment = Alignment.Top
    ) {

        Spacer(
            modifier = Modifier
                .size(50.dp)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(fraction = 0.5f)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(10.dp)) // creates an empty space between
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(10.dp)) // creates an empty space between
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(fraction = 0.9f)
                    .background(brush)
            )
        }
    }
}
