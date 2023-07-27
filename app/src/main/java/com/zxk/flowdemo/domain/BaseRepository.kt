package com.zxk.flowdemo.domain

import android.util.Log
import android.widget.Toast
import com.zxk.flowdemo.GlobalApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

/**
 * @ClassName com.zxk.flowdemo.domain
 * @author zhengxiaoke
 * @Description
 * @Date： 2023-07-27 14:39
 */
open class BaseRepository {


}

inline fun <reified T> request(
    scope: CoroutineScope,
    crossinline block: suspend () -> Flow<RequestCallback>,
    crossinline onSuccess: (T) -> Unit,
    crossinline onFail:((code:String,message:String)->Unit)={code,message->
        Toast.makeText(GlobalApp.application,"$message",Toast.LENGTH_SHORT).show()
    }
) {
    scope.launch {
        block.invoke()
            .flowOn(Dispatchers.IO)
            .collect {
                when (it) {
                    is RequestCallback.Success<*> -> {
                        onSuccess.invoke(it.data as T)
                    }

                    is RequestCallback.Fail -> {
                        onFail.invoke(it.code?:"",it.message?:"")
                    }
                }
            }
    }
}

inline fun <reified T> requestRemote(crossinline block: suspend () -> ApiResponse<T>): Flow<RequestCallback> {
    return flow { emit(block.invoke()) }
        .map {
            if (it.isSuccess()) {
                RequestCallback.Success(it.data, it.message)
            } else {
                RequestCallback.Fail(it.code, it.message)
            }
        }
        .catch {
            val apiException = ApiException.covert(it)
            emit(RequestCallback.Fail(apiException.code, apiException.message))
        }
}

class ApiException(val code: String, val message: String) {
    companion object {
        fun covert(throwable: Throwable): ApiException {
            return when (throwable) {
                is JSONException -> {
                    ApiException("-2", "数据解析错误")
                }

                else -> {
                    ApiException("-1", "网络错误")
                }
            }
        }
    }
}

data class ApiResponse<T>(
    val code: String? = null,
    val message: String? = null,
    val data: T? = null
) {
    fun isSuccess() = code == "0"
}

sealed class RequestCallback {
    data class Success<T>(val data: T?, val message: String?) : RequestCallback()
    data class Fail(val code: String?, val message: String?) : RequestCallback()
}