package com.example.upraxisexam.data.util

import retrofit2.Response

interface SafeAPICall {

    companion object {
        private const val NULL_API_RESPONSE_BODY_EXCEPTION_MESSAGE = "Response body is null"
        private const val UNSUCCESSFUL_API_RESPONSE_EXCEPTION_MESSAGE = "Response is unsuccessful"
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun <T : Any> callAPI(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
            throw NullAPIResponseBodyException(NULL_API_RESPONSE_BODY_EXCEPTION_MESSAGE)
        }
        throw UnsuccessfulAPIResponseException(UNSUCCESSFUL_API_RESPONSE_EXCEPTION_MESSAGE)
    }
}