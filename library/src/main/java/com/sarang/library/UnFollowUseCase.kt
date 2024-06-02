package com.sarang.library

interface UnFollowUseCase {
    suspend fun invoke(userId: Int): Boolean
}