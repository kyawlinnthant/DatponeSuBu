package com.klt.data.remote

import com.google.common.truth.Truth.assertThat
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

@ExperimentalCoroutinesApi
class PexelsServiceTest {

    private lateinit var service: PexelsService
    private lateinit var mockWebServer: MockWebServer


    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun enqueueResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api/$fileName")
        val mockResponse = MockResponse()
        val source = inputStream.source().buffer()
        mockWebServer.enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }

    //photos
    @Test
    @Throws(Exception::class)
    fun photosSuccess() = runTest {
        enqueueResponse(fileName = "photoSuccess.json")
        val response = service.getPhotos(1)
        val request = mockWebServer.takeRequest()

        assertThat(request.path).isEqualTo("/v1/curated?page=1&per_page=15")
        val data = response.body()
        assertThat(data?.page).isEqualTo(1)
        assertThat(data?.photosPerPage).isEqualTo(15)
        assertThat(data?.photos).isNotEmpty()
        assertThat(data?.totalResults).isEqualTo(8000)
        assertThat(data?.nextPage).isEqualTo("https://api.pexels.com/v1/curated/?page=2&per_page=15")
    }


    @Test(expected = MalformedJsonException::class)
    fun photosMalformed() = runTest {
        enqueueResponse(fileName = "photoMalformed.json")
        service.getPhotos(1)
    }

}