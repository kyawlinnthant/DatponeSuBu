package com.klt.datponesubu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.klt.datponesubu.app.theme.DatponeSuBuTheme
import com.klt.datponesubu.data.model.Photo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatponeSuBuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val vm: MainViewModel = hiltViewModel()
    val photos: LazyPagingItems<Photo> = vm.photos.collectAsLazyPagingItems()
    val page1 = vm.photo
    PhotosContent(photos = photos, photo = page1)
}

@Composable
fun PhotosContent(
    modifier: Modifier = Modifier,
    photos : LazyPagingItems<Photo>,
    photo : List<Photo>
){
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ){
        item {
            Text(text = photos.itemCount.toString())
        }
        item {
            Text(text = "Page 1 : ${photo.size}")
        }
        items(items = photos){ current ->
            PhotoItemView(photo = current?.id ?: "No" )
        }
        /*when {

            loadState.append is LoadState.Error -> {
                val e = friends.loadState.append as LoadState.Error
                item {
                    ScreenState(
                        title = e.error.message ?: "Error",
                        message = e.error.message ?: "Error",
                        onActionClicked = { retry() },
                        actionLabel = stringResource(id = R.string.retry),
                        actionColor = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            loadState.append.endOfPaginationReached -> {
                if (friends.itemCount != 0) {
                    item {
                        EndOfPagination()
                    }
                }
            }
        }*/
    }
}

@Composable
fun PhotoItemView(
    photo : String
){
    Text(text = photo)
}