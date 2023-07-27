package com.zxk.flowdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            flowOf(-1, 0, 1, 2, 3, -2)
                .retry(2)
                .onEach {
                    if (it < 0) {
                        throw RuntimeException("小于0")
                    }
                }
                .catch {
                    Log.e("TAG", it.message ?: "")
                    emit(6)
                }
                .onEach {
                    Log.d("TAG", "num=${it}")
                    throw RuntimeException("异常测试")
                }
                .catch {
                    Log.e("TAG", "123.${it.message ?: ""}")
                    emit(3)
                }
                .map {
                    it > 0
                }.collect {
                    Log.d("TAG", "boolean=${it}")
                }
        }

    }
}