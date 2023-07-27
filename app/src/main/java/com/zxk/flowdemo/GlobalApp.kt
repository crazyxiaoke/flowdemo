package com.zxk.flowdemo

import android.app.Application

/**
 * @ClassName com.zxk.flowdemo
 * @author zhengxiaoke
 * @Description
 * @Date： 2023-07-27 15:41
 */
object GlobalApp {
    lateinit var application:Application

    fun init(application: Application){
        this.application=application
    }

    fun get():Application{
        return application
    }
}