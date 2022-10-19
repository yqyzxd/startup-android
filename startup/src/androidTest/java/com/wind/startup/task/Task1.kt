package com.wind.startup.task

import com.wind.startup.MainTask

/**
 * Copyright (C), 2015-2022, 杭州迈优文化创意有限公司
 * FileName: Task1
 * Author: wind
 * Date: 2022/10/19 16:34
 * Description: 描述该类的作用
 * Path: 路径
 * History:
 *  <author> <time> <version> <desc>
 *
 */
class Task1 : MainTask() {
    override fun run() {
        println("Task1 run")
    }

    override fun needWait(): Boolean {
        return true
    }
}