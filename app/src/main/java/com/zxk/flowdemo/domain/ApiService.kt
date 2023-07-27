package com.zxk.flowdemo.domain

/**
 * @ClassName com.zxk.flowdemo.domain
 * @author zhengxiaoke
 * @Description
 * @Date： 2023-07-27 15:20
 */
object ApiService {


    fun login():ApiResponse<Int>{
        return ApiResponse("0","登录成功")
    }
}