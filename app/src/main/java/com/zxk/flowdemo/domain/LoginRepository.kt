package com.zxk.flowdemo.domain

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


/**
 * @ClassName com.zxk.flowdemo.domain
 * @author zhengxiaoke
 * @Description
 * @Date： 2023-07-27 15:18
 */
class LoginRepository : BaseRepository() {

    suspend fun login(): Flow<RequestCallback> {
        return requestRemote { ApiService.login() }
            .map {
                if (it is RequestCallback.Success<*>) {
                    RequestCallback.Success(1, "")
                } else {
                    it
                }
            }
            .onEach {
                if (it is RequestCallback.Success<*>) {
                    Log.d("TAG", "登录成功")
                }
            }
    }

}