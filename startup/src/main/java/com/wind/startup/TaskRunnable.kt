package com.wind.startup

import android.util.Log

class TaskRunnable(val task:ITask,val dispatcher: TaskDispatcher) :Runnable {


    override fun run() {

        task.waitToSatisfy()
        if (task.onlyRunOnMainProcess() && !TaskDispatcher.sMainProcess){
            if (TaskDispatcher.sDebug){
                Log.d("TaskDispatcher","skip ${task.javaClass.simpleName} task because of it's not in main process")
            }
        }else{
            if (TaskDispatcher.sDebug){
                Log.d("TaskDispatcher","run ${task.javaClass.simpleName}")
            }
            task.run()
        }

        dispatcher.satisfy(task)
        dispatcher.markTaskDone(task)

    }
}