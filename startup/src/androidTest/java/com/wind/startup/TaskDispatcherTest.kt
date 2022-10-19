package com.wind.startup

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wind.startup.task.Task1
import com.wind.startup.task.Task2
import com.wind.startup.task.Task3
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: TaskDispatcherTest
 * Author: wind
 * Date: 2022/10/19 16:32
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
@RunWith(AndroidJUnit4::class)
class TaskDispatcherTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.wind.startup.test", appContext.packageName)
    }
    @Test
    fun testTaskDispatcher(){
        println("testTaskDispatcher start")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        TaskDispatcher.initialize(appContext,true)
        val dispatcher:TaskDispatcher
        TaskDispatcher
            .newInstance().also { dispatcher=it }
            .add(Task1())
            .add(Task2())
            .add(Task3())
            .start()
        dispatcher.await()
        println("testTaskDispatcher end")
        Assert.assertEquals("com.wind.startup.test", appContext.packageName)
    }
}