package com.wind.startup

import android.util.Log

class TaskRunnable(private val task:ITask, private val dispatcher: TaskDispatcher) :Runnable {


    override fun run() {

        task.waitToSatisfy()
        if (task.onlyRunOnMainProcess() && !TaskDispatcher.isMainProcess()){
            if (TaskDispatcher.isDebug()){
                Log.d("TaskDispatcher","skip ${task.javaClass.simpleName} task because of it's not in main process")
            }
        }else{
            if (TaskDispatcher.isDebug()){
                Log.d("TaskDispatcher","run ${task.javaClass.simpleName}")
            }
            task.run()
        }

        dispatcher.satisfy(task)
        dispatcher.markTaskDone(task)

    }
}