package com.wind.startup

import android.content.Context
import com.wind.process.Processes
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TaskDispatcher private constructor() {

    /**
     * 所有添加的任务
     */
    private var mTasks = mutableListOf<ITask>()

    /**
     * List<ITask>任务集合 依赖 类型为Class<ITask>的任务
     */
    private var mDepends = mutableMapOf<Class<out ITask>, List<ITask>>()

    private var mWaitTasks = mutableListOf<ITask>()
    private var mCountDownLatch: CountDownLatch? = null


    companion object {
        private var mDebug: Boolean = false
        private var mMainProcess = false
        private var mInitialized = false

        @JvmStatic
        fun initialize(context: Context, debug: Boolean = false) {
            if (!mInitialized) {
                mInitialized = true
                mDebug = debug
                mMainProcess = Processes.isMainProcess(context)
            }
        }

        fun newInstance(): TaskDispatcher {
            if (!mInitialized) {
                throw IllegalStateException("Have you called initialize function?")
            }
            return TaskDispatcher()
        }


        internal fun isDebug() = mDebug
        internal fun isMainProcess() = mMainProcess
    }


    fun add(task: ITask): TaskDispatcher {
        analyze(task)
        mTasks.add(task)
        return this
    }

    /**
     *  get all who depends on me
     */
    private fun analyze(task: ITask) {
        if (task.needWait()) {
            mWaitTasks.add(task)
        }
        task.dependsOn()?.forEach {
            var list = mDepends[it]
            if (list == null) {
                list = mutableListOf()
                mDepends[it] = list
            }
            (list as MutableList).add(task)
        }

    }

    /**
     * start all tasks
     */
    fun start() {
        //对tasks 进行有向无环图构造，并输出拓扑结构
        val graph = buildGraph(mTasks, mDepends)
        //输出graph
        val topologicalSortedList = graph.topologicalSort()

        val result = mutableListOf<ITask>()
        for (i in 0 until mTasks.size) {
            val index = topologicalSortedList[i]
            result.add(mTasks[index])
        }

        if (mDebug) {
            println("--------TaskDispatcher---------")
            result.forEach {
                print("${it.javaClass.simpleName} ")
            }
            println()
            println("--------TaskDispatcher---------")
        }
        if (mWaitTasks.isNotEmpty()) {
            mCountDownLatch = CountDownLatch(mWaitTasks.size)
        }

        result.forEach {
            it.executor().execute(TaskRunnable(it, this))
        }

    }

    private fun buildGraph(
        tasks: MutableList<ITask>,
        depends: MutableMap<Class<out ITask>, List<ITask>>
    ): Graph {
        val graph = Graph(tasks.size)
        val taskClasses = mutableListOf<Class<ITask>>()
        tasks.forEach {
            taskClasses.add(it.javaClass)
        }

        depends.forEach {
            val from = taskClasses.indexOf(it.key)
            it.value.forEach { task ->
                val to = tasks.indexOf(task)
                graph.addEdge(from, to)
            }
        }

        return graph
    }

    internal fun satisfy(task: ITask) {
        val tasks = mDepends[task.javaClass]
        tasks?.forEach {
            it.satisfy()
        }
    }

    internal fun markTaskDone(task: ITask) {
        if (task.needWait()) {
            mCountDownLatch?.countDown()
        }
    }

    /**
     * wait all tasks to finish
     */
    fun await(timeoutMillis:Long = 0) {

        if (mWaitTasks.isNotEmpty()) {
            try {
                if (timeoutMillis <= 0){
                    mCountDownLatch?.await()
                }else{
                    mCountDownLatch?.await(timeoutMillis,TimeUnit.MILLISECONDS)
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}