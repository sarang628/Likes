package com.sarang.library

import kotlinx.coroutines.flow.Flow

interface IsLoginUseCase {
    fun invoke(): Flow<Boolean>
}