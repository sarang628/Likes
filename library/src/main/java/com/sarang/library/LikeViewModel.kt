package com.sarang.library

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeViewModel @Inject constructor(
    private val getLikeUseCase: GetLikesUseCase,
    private val followUseCase: FollowUseCase,
    private val unFollowUseCase: UnFollowUseCase,
    private val isLoginUseCase: IsLoginUseCase
) : ViewModel() {
    val tag = "__LikeViewModel"
    private val _uiState: MutableStateFlow<LikeUiState> = MutableStateFlow(LikeUiState.Loading)
    val isLogin = isLoginUseCase.invoke()

    val uiState: StateFlow<LikeUiState> = combine(
        _uiState, isLogin

    ) { state, login ->
        if (!login) {
            LikeUiState.NeedLogin
        } else {
            if (state == LikeUiState.CheckLogin || state == LikeUiState.NeedLogin) {
                LikeUiState.Loading
            } else {
                state
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LikeUiState.CheckLogin
    )


    fun loadLikes(reviewId: Int) {
        viewModelScope.launch {
            _uiState.value = getLikeUseCase.invoke(reviewId)
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