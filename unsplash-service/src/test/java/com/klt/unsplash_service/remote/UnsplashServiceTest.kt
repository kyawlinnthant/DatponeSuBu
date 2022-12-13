package com.klt.unsplash_service.remote

import com.google.common.truth.Truth
import com.google.gson.stream.MalformedJsonException
import com.klt.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class UnsplashServiceTest {

    private lateinit var service: UnsplashService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UnsplashService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val mockResponse = MockResponse()
        val source = inputStream.source().buffer()
        mockWebServer.enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }

    @Test
    @Throws(Exception::class)
    fun `photos from unsplash successfully parse to DTO`() = runTest {
        val requestedPageNumber = 1
        enqueueResponse(fileName = "photos.json")
        val response = service.getPhotos(
            pageNumber = requestedPageNumber
        )
        val request = mockWebServer.takeRequest()
        //test request is correct?
        Truth.assertThat(request.method).isEqualTo("GET")
        Truth.assertThat(request.path)
            .isEqualTo("/" + UnsplashService.PHOTOS +
                    "?page=$requestedPageNumber" +
                    "&per_page=${Constants.LOAD_SIZE}" +
                    "&order_by=${Constants.LOAD_SEQUENCE}")

        val data = response.body()
        //test response is correct?
        Truth.assertThat(data?.size).isEqualTo(15)
    }

    @Test(expected = MalformedJsonException::class)
    fun `malformed json throws MalformedJsonException`() = runTest {
        enqueueResponse(fileName = "malformed.json")
        service.getPhotos(
            pageNumber = 1,
            photosPerPage = 10,
            orderBy = "latest"
        )
    }


}