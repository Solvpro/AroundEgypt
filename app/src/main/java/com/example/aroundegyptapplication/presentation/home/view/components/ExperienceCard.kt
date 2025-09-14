package com.example.aroundegyptapplication.presentation.home.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PanoramaPhotosphere
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aroundegyptapplication.R
import com.example.aroundegyptapplication.domain.model.Experience
import com.example.aroundegyptapplication.presentation.theme.HeartOrange

@Composable
fun ExperienceCard(
    experience: Experience,
    onClick: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    isHorizontal: Boolean = false
) {
    Column(
        modifier = modifier
            .then(if (!isHorizontal) Modifier.fillMaxWidth() else Modifier)
            .clickable { onClick() }
    ) {
        // Image Card with Overlays
        Box(
            modifier = Modifier
                .then(if (isHorizontal) Modifier.width(336.dp) else Modifier.fillMaxWidth())
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Background Image
            AsyncImage(
                model = experience.coverPhoto,
                contentDescription = experience.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Subtle gradient for text readability
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f)
                            )
                        )
                    )
            )

            // RECOMMENDED Badge (only for horizontal cards)
            if (experience.recommended && isHorizontal) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.8f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Recommended",
                            tint = HeartOrange,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "RECOMMENDED",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Info Icon - Top Right
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "Info",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp),
                tint = Color.White
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_360),
                contentDescription = "360 View",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                tint = Color.White
            )

            // Views Counter - Bottom Left
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = "Views",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Text(
                    text = "${experience.viewsNo}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = "Gallery",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(24.dp),
                tint = Color.White
            )
        }

        // Title and Like Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title
            Text(
                text = experience.title,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 0.15.sp
            )

            // Like Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = "${experience.likesNo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    letterSpacing = 0.15.sp
                )

                IconButton(
                    onClick = onLike,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (experience.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (experience.isLiked) "Unlike" else "Like",
                        tint = HeartOrange,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}