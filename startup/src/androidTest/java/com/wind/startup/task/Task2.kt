package com.wind.startup.task

import com.wind.startup.Task

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: Task2
 * Author: wind
 * Date: 2022/10/19 16:35
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class Task2 : Task() {
    override fun run() {
        Thread.sleep(1000)
        println("Task2 run")
    }

    override fun needWait(): Boolean {
        return true
    }
}