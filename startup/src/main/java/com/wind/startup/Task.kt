package com.wind.startup

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

abstract class Task : ITask {

    private val mDependsLatch = CountDownLatch(dependsOn()?.size ?: 0)

    override fun needWait(): Boolean {
        return false
    }

    override fun dependsOn(): List<Class<out ITask>>? {
       return null
    }


    override fun executor(): Executor {
        return if (boundCpu()) ExecutorProvider.cpuExecutor else ExecutorProvider.iOExecutor
    }

    override fun boundCpu(): Boolean {
        return true
    }
    override fun onlyRunOnMainProcess(): Boolean {
        return false
    }
    override fun waitToSatisfy() {
        try {
            mDependsLatch.await()
        }catch (e: InterruptedException){
            e.printStackTrace()
        }

    }

    override fun satisfy(){
        mDependsLatch.countDown()
    }
}