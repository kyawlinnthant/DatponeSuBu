package com.klt.pexels_service.remote

import com.google.common.truth.Truth.assertThat
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
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class PexelsServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: PexelsService

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
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val mockResponse = MockResponse()
        val source = inputStream.source().buffer()
        mockWebServer.enqueue(
            mockResponse.setBody(source.readString(Charsets.UTF_8))
        )
    }

    //get photos with pagination
    @Test
    @Throws(Exception::class)
    fun `getting photos clients error returns 4xx`() = runTest {
        val requestPageNumber = 100
        val expectedRequest = MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockWebServer.enqueue(expectedRequest)
        val actualResponse = service.getPhotos(
            pageNumber = requestPageNumber
        )
        val request = mockWebServer.takeRequest()
        //test request
        assertThat(request.path).isEqualTo("/v1/curated?page=$requestPageNumber&per_page=${Constants.LOAD_SIZE}")
        assertThat(request.method).isEqualTo("GET")
        //test response
        assertThat(actualResponse.code()).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
        assertThat(actualResponse.message()).isEqualTo("Client Error")

    }

    @Test
    @Throws(Exception::class)
    fun `getting photos server error returns 5xx`() = runTest {
        val requestPageNumber = 100
        val expectedRequest = MockResponse().setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockWebServer.enqueue(expectedRequest)
        val actualResponse = service.getPhotos(
            pageNumber = requestPageNumber
        )
        val request = mockWebServer.takeRequest()
        //test request
        assertThat(request.path).isEqualTo("/v1/curated?page=$requestPageNumber&per_page=${Constants.LOAD_SIZE}")
        assertThat(request.method).isEqualTo("GET")
        //test response
        assertThat(actualResponse.code()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertThat(actualResponse.message()).isEqualTo("Server Error")
    }

    @Test
    @Throws(Exception::class)
    fun `getting photos with non-exist page returns empty data`() = runTest {
        val nonExistedPage = 1234567890
        enqueueResponse(fileName = "empty.json")
        val response = service.getPhotos(nonExistedPage)
        val request = mockWebServer.takeRequest()

        //test request is correct?
        assertThat(request.method).isEqualTo("GET")
        assertThat(request.path).isEqualTo("/v1/curated?page=$nonExistedPage&per_page=${Constants.LOAD_SIZE}")

        val data = response.body()
        //test response is correctly parse?
        assertThat(data?.pageNumber).isEqualTo(1)
        assertThat(data?.photosPerPage).isEqualTo(0)
        assertThat(data?.photos).isEmpty()
        assertThat(data?.totalResults).isEqualTo(8000)
        assertThat(data?.nextPage).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun `getting photos successfully parse to DTO`() = runTest {
        val requestedPageNumber = 1
        enqueueResponse(fileName = "data.json")
        val response = service.getPhotos(requestedPageNumber)
        val request = mockWebServer.takeRequest()

        //test request is correct?
        assertThat(request.path).isEqualTo("/v1/curated?page=$requestedPageNumber&per_page=${Constants.LOAD_SIZE}")
        assertThat(request.method).isEqualTo("GET")
        val data = response.body()
        //test response is correctly parse?
        assertThat(data?.pageNumber).isEqualTo(1)
        assertThat(data?.photosPerPage).isEqualTo(15)
        assertThat(data?.photos).isNotEmpty()
        assertThat(data?.totalResults).isEqualTo(8000)
        assertThat(data?.nextPage).isEqualTo("https://api.pexels.com/v1/curated/?page=2&per_page=15")
    }

    @Test(expected = MalformedJsonException::class)
    fun `malformed json meets MalformedJsonException`() = runTest {
        enqueueResponse(fileName = "malformed.json")
        service.getPhotos(1)
    }

}