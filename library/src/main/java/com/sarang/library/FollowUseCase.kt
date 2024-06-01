package com.sarang.library

interface FollowUseCase {
    suspend fun invoke(userId: Int): Boolean
}