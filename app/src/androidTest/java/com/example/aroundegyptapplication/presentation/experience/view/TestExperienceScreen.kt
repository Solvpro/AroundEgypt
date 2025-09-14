package com.example.aroundegyptapplication.presentation.experience.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aroundegyptapplication.R
import com.example.aroundegyptapplication.presentation.experience.model.ExperienceUiState
import com.example.aroundegyptapplication.presentation.experience.viewmodel.ExperienceViewModel
import com.example.aroundegyptapplication.presentation.theme.BackgroundWhite
import com.example.aroundegyptapplication.presentation.theme.HeartOrange
import com.example.aroundegyptapplication.presentation.theme.LineGray
import com.example.aroundegyptapplication.presentation.theme.TextOrange

@Composable
fun TestExperienceScreen(
    uiState: ExperienceUiState,
    onBackClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = Color.White
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = TextOrange
                        )
                    }
                }

                uiState.experience != null -> {
                    val experience = uiState.experience

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Hero Image Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            // Background Image
                            AsyncImage(
                                model = experience?.coverPhoto,
                                contentDescription = experience?.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Subtle gradient overlay for text readability
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.2f),
                                                Color.Transparent,
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.3f)
                                            )
                                        )
                                    )
                            )

                            // Back Arrow - Top Left
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(16.dp)
                                    .size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Button(
                                onClick = {  },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .width(156.dp)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BackgroundWhite,
                                    contentColor = TextOrange
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "EXPLORE NOW",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            // Bottom Section with Views and Gallery
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                // Views Counter
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_eye),
                                        contentDescription = "Views",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        text = "${experience?.viewsNo} views",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Gallery Icon
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_gallery),
                                    contentDescription = "Gallery",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White
                                )
                            }
                        }

                        // Content Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(20.dp)
                        ) {
                            // Title and Like Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Title
                                    Text(
                                        text = experience?.title ?: "",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        letterSpacing = 0.15.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    // Location
                                    experience?.city?.let { city ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 8.dp)
                                        ) {
                                            Text(
                                                text = city,
                                                fontSize = 14.sp,
                                                color = Color(0xFF757575),
                                                fontWeight = FontWeight.Normal
                                            )
                                        }
                                    }
                                }

                                // Like Section
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 16.dp)
                                ) {

                                    IconButton(
                                        onClick = { },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.IosShare,
                                            contentDescription = "Share",
                                            tint = HeartOrange,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    IconButton(
                                        onClick = onLikeClick,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (experience?.isLiked == true)
                                                Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = if (experience?.isLiked == true) "Unlike" else "Like",
                                            tint = HeartOrange,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Text(
                                        text = "${experience?.likesNo}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                                thickness = 0.5.dp,
                                color = LineGray
                            )

                            // Description Section
                            Column(
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Description",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Text(
                                    text = experience?.description ?: "",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    lineHeight = 22.sp,
                                    letterSpacing = 0.25.sp
                                )
                            }
                        }
                    }
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.Red,
                            fontSize = 16.sp
                        )

                        Button(
                            onClick = { },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}