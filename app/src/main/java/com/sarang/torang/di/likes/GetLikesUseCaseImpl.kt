package com.sarang.torang.di.likes

import android.util.Log
import com.sarang.library.FollowUseCase
import com.sarang.library.GetLikesUseCase
import com.sarang.library.Like
import com.sarang.library.LikeUiState
import com.sarang.library.UnFollowUseCase
import com.sarang.torang.BuildConfig
import com.sarang.torang.api.ApiLike
import com.sarang.torang.repository.FollowRepository
import com.sarang.torang.session.SessionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LikesModule {

    @Provides
    fun provideGetLikesUseCase(apilike: ApiLike, sessionService: SessionService): GetLikesUseCase {
        return object : GetLikesUseCase {
            override suspend fun invoke(reviewId: Int): LikeUiState {
                Log.d("__provideGetLikesUseCase", "invoke: $reviewId")
                try {
                    val result = apilike.getLikeUserByReviewId(
                        Int = reviewId.toString(),
                        auth = sessionService.getToken() ?: ""
                    )
                    Log.d("__provideGetLikesUseCase", "follower Ids: ${result.map { it.followerId }}")
                    return LikeUiState.Success(
                        result.map {
                            Like(
                                url = BuildConfig.PROFILE_IMAGE_SERVER_URL + it.profilePicUrl,
                                name = it.userName,
                                isFollow = it.isFollow == 1,
                                followerId = it.followerId
                            )
                        }
                    )
                } catch (e: Exception) {
                    return LikeUiState.Error
                }
            }
        }
    }

    @Provides
    fun provideFollowUseCase(followRepository: FollowRepository): FollowUseCase {
        return object : FollowUseCase {
            override suspend fun invoke(userId: Int): Boolean {
                try {
                    followRepository.follow(userId)
                    return true
                } catch (e: Exception) {
                    return false
                }
            }
        }
    }

    @Provides
    fun provideUnFollowUseCase(followRepository: FollowRepository): UnFollowUseCase {
        return object : UnFollowUseCase {
            override suspend fun invoke(userId: Int): Boolean {
                try {
                    followRepository.unFollow(userId)
                    return true
                } catch (e: Exception) {
                    return false
                }
            }
        }
    }
}