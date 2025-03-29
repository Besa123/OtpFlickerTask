package com.balazs.otpflickertask.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.balazs.otpflickertask.data.remote.FlickerApi
import com.balazs.otpflickertask.domain.model.Pictures.Picture

class FlickrPicturePaging(
    private val flickerApi: FlickerApi,
    private val searchTerm: String,
) : PagingSource<Int, Picture>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Picture> {
        val currentPage = params.key ?: STARTING_KEY
        return try {
            flickerApi.getPictures(
                searchTerm = searchTerm,
                page = currentPage,
                pageSize = PAGE_SIZE
            ).fold(
                onSuccess = { result ->
                    val endOfPagingReached =
                        result.totalPageAmount?.let { currentPage >= it } ?: true

                    if (result.pictureUrlList?.isNotEmpty() == true) {
                        LoadResult.Page(
                            data = result.pictureUrlList,
                            prevKey = if (currentPage == STARTING_KEY) null else currentPage - 1,
                            nextKey = if (endOfPagingReached) null else currentPage + 1
                        )
                    } else {
                        LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null,
                        )
                    }
                },
                onFailure = {
                    LoadResult.Error(it)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Picture>): Int? {
        return state.anchorPosition
    }


    companion object {
        const val PAGE_SIZE = 20

        private const val STARTING_KEY = 1
    }

}