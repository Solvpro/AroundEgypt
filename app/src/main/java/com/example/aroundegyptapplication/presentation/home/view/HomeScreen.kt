package com.example.aroundegyptapplication.presentation.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aroundegyptapplication.presentation.home.view.components.ExperienceCard
import com.example.aroundegyptapplication.presentation.home.view.components.TopSearchBar
import com.example.aroundegyptapplication.presentation.home.view.components.ShimmerExperienceCard
import com.example.aroundegyptapplication.presentation.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state

    // Error handling with SnackBar
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                onSearch = viewModel::onSearchImeAction,
                onClear = viewModel::clearSearch
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            when {
                // Initial Loading State
                uiState.isLoading -> {
                    InitialLoadingState()
                }

                // Search Results State
                uiState.searchQuery.isNotEmpty() -> {
                    SearchResultsSection(
                        searchQuery = uiState.searchQuery,
                        searchResults = uiState.searchResults,
                        isSearching = uiState.isSearching,
                        onExperienceClick = { experienceId ->
                            navController.navigate("experience/$experienceId")
                        },
                        onLikeClick = { experienceId ->
                            viewModel.onLikeExperience(experienceId)
                        }
                    )
                }

                // Main Content
                else -> {
                    MainContentSection(
                        uiState = uiState,
                        onRefresh = { viewModel.onRefresh() },
                        onExperienceClick = { experienceId ->
                            navController.navigate("experience/$experienceId")
                        },
                        onLikeClick = { experienceId ->
                            viewModel.onLikeExperience(experienceId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun InitialLoadingState() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // Welcome Section Shimmer
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                repeat(3) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // Recommended Section Shimmer
        item {
            ShimmerBox(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.6f)
                    .height(22.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Horizontal Cards Shimmer
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(2) {
                    ShimmerExperienceCard(
                        modifier = Modifier.width(336.dp),
                        isHorizontal = true
                    )
                }
            }
        }

        // Most Recent Section Shimmer
        item {
            Spacer(modifier = Modifier.height(32.dp))
            ShimmerBox(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.5f)
                    .height(22.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Vertical Cards Shimmer
        items(3) {
            ShimmerExperienceCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                isHorizontal = false
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SearchResultsSection(
    searchQuery: String,
    searchResults: List<com.example.aroundegyptapplication.domain.model.Experience>,
    isSearching: Boolean,
    onExperienceClick: (String) -> Unit,
    onLikeClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when {
            isSearching -> {
                items(3) {
                    ShimmerExperienceCard(isHorizontal = false)
                }
            }

            searchResults.isEmpty() -> {
                item {
                    EmptySearchState(
                        searchQuery = searchQuery,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            else -> {
                items(searchResults) { experience ->
                    ExperienceCard(
                        experience = experience,
                        onClick = { onExperienceClick(experience.id) },
                        onLike = { onLikeClick(experience.id) },
                        isHorizontal = false
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContentSection(
    uiState: com.example.aroundegyptapplication.presentation.home.model.HomeUiState,
    onRefresh: () -> Unit,
    onExperienceClick: (String) -> Unit,
    onLikeClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        // Welcome Section
        item {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 20.dp
                )
            ) {
                Text(
                    text = "Welcome!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Now you can explore any experience in 360 degrees and get all the details about it all in one place.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )
            }
        }

        // Recommended Experiences Section
        item {
            SectionHeader(
                title = "Recommended Experiences",
                showRetry = uiState.recommendedExperiences.isEmpty() && !uiState.isLoading,
                onRetry = onRefresh
            )
        }

        // Recommended Experiences Content
        item {
            when {
                uiState.recommendedExperiences.isEmpty() || uiState.isLoading -> {
                    // Loading state for recommended
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(2) {
                            ShimmerExperienceCard(
                                modifier = Modifier.width(336.dp),
                                isHorizontal = true
                            )
                        }
                    }
                }

                else -> {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        state = rememberLazyListState()
                    ) {
                        items(uiState.recommendedExperiences) { experience ->
                            ExperienceCard(
                                experience = experience,
                                onClick = { onExperienceClick(experience.id) },
                                onLike = { onLikeClick(experience.id) },
                                isHorizontal = true,
                                modifier = Modifier.width(336.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Most Recent Section
        item {
            SectionHeader(
                title = "Most Recent",
                showRetry = uiState.recentExperiences.isEmpty() && !uiState.isLoading,
                onRetry = onRefresh
            )
        }

        when {
            uiState.recentExperiences.isEmpty() || uiState.isLoading || uiState.isRefreshing -> {
                items(4) {
                    ShimmerExperienceCard(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        isHorizontal = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            else -> {
                items(uiState.recentExperiences) { experience ->
                    ExperienceCard(
                        experience = experience,
                        onClick = { onExperienceClick(experience.id) },
                        onLike = { onLikeClick(experience.id) },
                        isHorizontal = false,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    showRetry: Boolean = false,
    onRetry: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            letterSpacing = 0.15.sp
        )

        if (showRetry) {
            TextButton(onClick = onRetry) {
                Text(
                    text = "Retry",
                    fontSize = 14.sp,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun EmptyStateCard(
    title: String,
    subtitle: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFFBDBDBD)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2196F3)
                )
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFFBDBDBD)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No results found",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Text(
            text = "We couldn't find any experiences matching \"$searchQuery\"",
            fontSize = 14.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
        )
    }
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(4.dp)
            )
    )
}