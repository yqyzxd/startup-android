package com.wind.startup

import java.util.concurrent.Executor

interface ITask {

    /**
     * 是否需要等待执行结束
     */
    fun needWait():Boolean

    /**
     * 被等待的任务完成通知
     */
    fun satisfy()

    /**
     * 如果依赖的其他任务，那么需要调用该方法等待 被等待的任务 完成
     */
    fun waitToSatisfy()

    /**
     * 依赖的任务
     */
    fun dependsOn():List<Class<out ITask>>?

    /**
     * 用于执行任务的executor
     */
    fun executor():Executor

    /**
     * cpu密集型还是io密集型
     */
    fun boundCpu():Boolean

    /**
     * 执行具体任务
     */
    fun run()

    /**
     * 是否只在主进程中运行
     */
    fun onlyRunOnMainProcess():Boolean
}