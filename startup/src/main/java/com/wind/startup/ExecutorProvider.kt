package com.wind.startup

import java.util.concurrent.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object ExecutorProvider {


    private var CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private var CORE_POOL_SIZE = Math.max(2,Math.min(CPU_COUNT-1,5))
    private var MAXIMUM_POOL_SIZE = CORE_POOL_SIZE
    private var mQueue:BlockingQueue<Runnable> = LinkedBlockingQueue()
    private var KEEP_ALIVE_SECONDS:Long = 10
    private val sThreadFactory = DefaultThreadFactory()

    var cpuExecutor:ThreadPoolExecutor
        private set
    var iOExecutor:ExecutorService
        private set
    init {

        cpuExecutor=ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,KEEP_ALIVE_SECONDS,TimeUnit.SECONDS,
            mQueue,sThreadFactory)
        cpuExecutor.allowCoreThreadTimeOut(true)

        iOExecutor= Executors.newCachedThreadPool(sThreadFactory)

    }


    /*fun provideCpuExecutor():ThreadPoolExecutor{
        return cpuExecutor
    }

    fun provideIoExecutor():ExecutorService{
        return iOExecutor
    }*/


    private class DefaultThreadFactory:ThreadFactory{
        private val mCount = AtomicInteger(1)

        override fun newThread(r: Runnable): Thread? {
            return Thread(r, "Executors #" + mCount.getAndIncrement())
        }

    }
}