package com.wind.startup

import android.content.Context
import java.util.concurrent.CountDownLatch

class TaskDispatcher private constructor(){

    /**
     * 所有添加的任务
     */
    private var mTasks = mutableListOf<ITask>()

    /**
     * List<ITask>任务集合 依赖 类型为Class<ITask>的任务
     */
    private var mDepends= mutableMapOf<Class<out ITask>,List<ITask>>()

    private var mWaitTasks=mutableListOf<ITask>()
    private var mCountDownLatch:CountDownLatch?=null
    companion object{
        var sDebug:Boolean=false
        var sMainProcess=false
        private var sInited=false
        fun init(context: Context){
            sInited=true
            sMainProcess=ProcessUtil.isMainProcess(context)
        }

        fun setDebug(debug:Boolean){
            sDebug=debug
        }

        fun newInstance():TaskDispatcher{
            if (!sInited){
                throw IllegalStateException("must call init(context) first")
            }
            return TaskDispatcher()
        }
    }



    fun add(task: ITask): TaskDispatcher {
        //todo 分析依赖 谁依赖我 而不是我依赖谁
        analyze(task)
        mTasks.add(task)
        return this
    }

    private fun analyze(task: ITask) {
        if (task.needWait()){
            mWaitTasks.add(task)
        }
        task.dependsOn()?.forEach {
           var list= mDepends.get(it)
            if (list==null){
                list= mutableListOf()
                mDepends.put(it, list)
            }
            (list as MutableList).add(task)
        }

    }


    fun start() {
        //对tasks 进行有向无环图构造，并输出拓扑结构
        var graph=buildGraph(mTasks,mDepends)
        //输出graph
        var topologicalSortedList=graph.topologicalSort()

        var result= mutableListOf<ITask>()
        for (i in 0 until mTasks.size){
            var index=topologicalSortedList.get(i)

            result.add(mTasks.get(index))

        }

        if (sDebug){
            println("--------TaskDispatcher---------")
            result.forEach {
                print("${it.javaClass.simpleName} ")
            }
            println()
            println("--------TaskDispatcher---------")
        }


        result.forEach {
            it.executor().execute(TaskRunnable(it,this))
        }

    }

    private fun buildGraph(tasks: MutableList<ITask>, depends: MutableMap<Class<out ITask>, List<ITask>>): Graph {
        var graph=Graph(tasks.size)
        var taskClasses= mutableListOf<Class<ITask>>()
        tasks.forEach {
            taskClasses.add(it.javaClass)
        }

        depends.forEach{
            val from=taskClasses.indexOf(it.key)
            it.value.forEach {
                val to=tasks.indexOf(it)
                graph.addEdge(from,to)
            }

        }

        return graph
    }

    fun satisfy(task: ITask) {
        val tasks=mDepends.get(task.javaClass)
        tasks?.forEach {
            it.satisfy()
        }
    }



    fun await(){

        if (mWaitTasks.isNotEmpty()) {
            mCountDownLatch= CountDownLatch(mWaitTasks.size)
            try {
                mCountDownLatch?.await()
            }catch (e: InterruptedException){
                e.printStackTrace()
            }

        }
    }

    fun markTaskDone(task: ITask) {
        if (task.needWait()){
            mCountDownLatch?.countDown()
        }
    }


}