package com.sarang.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val useCase: GetLikesUseCase,
    private val followUseCase: FollowUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<LikeUiState> = MutableStateFlow(LikeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadLikes(reviewId: Int) {
        viewModelScope.launch {
            _uiState.value = useCase.invoke(reviewId)
        }
    }

    fun follow(userId: Int) {
        viewModelScope.launch {
            followUseCase.invoke(userId)
        }
    }
}