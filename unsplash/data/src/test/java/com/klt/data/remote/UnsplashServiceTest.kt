package com.klt.data.remote

import com.google.common.truth.Truth
import com.google.gson.stream.MalformedJsonException
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
        enqueueResponse(fileName = "photos.json")
        val response = service.getPhotos(
            pageNumber = 1,
            photosPerPage = 15,
            orderBy = "latest"
        )
        val request = mockWebServer.takeRequest()

        Truth.assertThat(request.path).isEqualTo("/photos?page=1&per_page=15&order_by=latest")
        val data = response.body()
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