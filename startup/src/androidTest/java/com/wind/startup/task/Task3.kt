package com.wind.startup.task

import com.wind.startup.Task

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: Task3
 * Author: wind
 * Date: 2022/10/19 16:36
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class Task3 : Task() {
    override fun run() {
        Thread.sleep(2000)
        println("Task3 run")
    }

    override fun needWait(): Boolean {
        return true
    }


}