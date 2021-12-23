package com.wind.startup

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * 主线程中执行的任务
 */
abstract class MainTask:Task() {

    override fun executor(): Executor {
        return MainExecutor()
    }


    private class MainExecutor : Executor{
        private var mMainHandler= Handler(Looper.getMainLooper())
        override fun execute(runnable: Runnable) {
            if (Looper.myLooper() == Looper.getMainLooper()){
                runnable.run()
            }else{
                mMainHandler.post(runnable)
            }

        }

    }

    override fun onlyRunOnMainProcess(): Boolean {
        return true
    }
}