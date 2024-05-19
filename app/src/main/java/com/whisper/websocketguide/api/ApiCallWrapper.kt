package com.whisper.websocketguide.api

import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

object ApiCallWrapper {

    suspend fun <R> call(apiCall: suspend () -> R, onError: (Int, Int?, ResponseBody?, String?) -> Unit): R? {
        try {
            return apiCall()
        } catch (ex: HttpException) {
            onError(0, ex.code(), ex.response()?.errorBody(), null)
        } catch (ex: ConnectException) {
            onError(1, null, null, ex.message)
        } catch (ex: UnknownHostException) {
            onError(2, null, null, ex.message)
        } catch (ex: Exception) {
            onError(3, null, null, ex.message)
        }

        return null
    }
}