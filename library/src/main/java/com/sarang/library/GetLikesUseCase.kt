package com.sarang.library

interface GetLikesUseCase {
    suspend fun invoke(reviewId: Int): LikeUiState
}