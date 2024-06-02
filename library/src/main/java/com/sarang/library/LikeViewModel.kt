package com.sarang.library

import android.util.Log
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
    private val unFollowUseCase: UnFollowUseCase,
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
            val uiState = _uiState.value
            if (uiState is LikeUiState.Success) {
                uiState.list.find { it.followerId == userId }?.let {
                    if (!it.isFollow) {
                        followUseCase.invoke(userId)
                        _uiState.update {
                            (it as LikeUiState.Success).copy(it.list.map { it ->
                                if (it.followerId == userId) {
                                    Log.d("__LikeViewModel", "follow: ${it.followerId} == $userId")
                                    it.copy(isFollow = true)
                                } else it
                            })
                        }
                    } else {
                        unFollowUseCase.invoke(userId)
                        _uiState.update {
                            (it as LikeUiState.Success).copy(it.list.map { it ->
                                if (it.followerId == userId) it.copy(
                                    isFollow = false
                                ) else it
                            })
                        }
                    }
                }
            }
        }
    }
}