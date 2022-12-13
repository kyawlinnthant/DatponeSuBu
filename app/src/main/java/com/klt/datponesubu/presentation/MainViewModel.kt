package com.klt.datponesubu.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.klt.datponesubu.data.model.Photo
import com.klt.datponesubu.data.repository.NetworkRepository
import com.klt.util.Constants
import com.klt.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: NetworkRepository
) : ViewModel() {

    var photos: Flow<PagingData<Photo>> = emptyFlow()
        private set

    val photo = mutableStateListOf<Photo>()

    init {
        viewModelScope.launch {
//            repo.getPhotosPageable().collectLatest {
//                photos = flow {
//                    emit(it)
//                }.cachedIn(this)
//            }
//            repo.getPhotos(pageNumber = Constants.INITIAL_PAGE).collectLatest {
//                photo.addAll(it.data ?: emptyList())
//            }
            repo.getPexels().collectLatest {
                when (it) {
                    is Result.Error -> {

                    }
                    is Result.Success -> {
                        photo.addAll(it.data)
                    }
                }
            }
        }
    }
}