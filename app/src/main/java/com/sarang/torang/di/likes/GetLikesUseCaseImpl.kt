package com.sarang.torang.di.likes

import android.util.Log
import com.sarang.library.FollowUseCase
import com.sarang.library.GetLikesUseCase
import com.sarang.library.Like
import com.sarang.library.LikeUiState
import com.sarang.torang.BuildConfig
import com.sarang.torang.api.ApiLike
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
    fun provideFollowUseCase(apilike: ApiLike, sessionService: SessionService): FollowUseCase {
        return object : FollowUseCase {
            override suspend fun invoke(reviewId: Int): Boolean {
                Log.d("__provideGetLikesUseCase", "invoke: $reviewId")
                try {
                    return true

                } catch (e: Exception) {
                    return false
                }
            }
        }
    }
}