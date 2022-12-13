package com.klt.datponesubu.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.klt.datponesubu.data.model.Photo
import com.klt.datponesubu.data.model.transform
import com.klt.pexels_service.remote.PexelsService
import com.klt.unsplash_service.remote.UnsplashService
import com.klt.util.Constants
import javax.inject.Inject

class PhotoPagingSource @Inject constructor(
    private val pexels: PexelsService,
    private val unsplash: UnsplashService
) : PagingSource<Int, Photo>() {


    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val currentPage = params.key ?: Constants.INITIAL_PAGE

        return try {

            val combinedPhotos = mutableListOf<Photo>()
            var nextKey: Int? = null

            val pexelsCall = try {
                val call = pexels.getPhotos(pageNumber = currentPage)
                val result = call.body()?.photos?.map { it.transform() }
                result?.let {
                    combinedPhotos.addAll(it)
                    nextKey = currentPage + 1
                }
                result?.toMutableList()
            } catch (e: Exception) {
                null
            }
            val unsplashCall = try {
                val call = unsplash.getPhotos(pageNumber = currentPage)
                val result = call.body()?.map { it.transform() }
                result?.let {
                    combinedPhotos.addAll(it)
                    nextKey = currentPage + 1
                }
                result?.toMutableList()
            } catch (e: Exception) {
                null
            }
            LoadResult.Page(
                data = combinedPhotos,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}