package com.wind.startup

import java.util.*

class Graph(
    private val  vertices:Int
){

    private val mAdj:Array<MutableList<Int>?> = arrayOfNulls(vertices)
    private val mInDegrees:Array<Int> = Array(vertices){
        0
    }

    init {

        for (i in 0 until vertices){
            mAdj[i]= mutableListOf()
        }

    }

    fun addEdge(from:Int,to:Int){
        mAdj[from]?.add(to)
        //增加to的入度
        mInDegrees[to]++
    }


    /**
     * 入度法对图进行拓扑排序
     * 步骤：
     *      1. 以邻接表为存储结构
     *      2. 建立入度为0的顶点栈，把领接表中所有入度为0的顶点入栈
     *      3. 栈非空时，输出栈顶元素Vj并退栈；在领接表中查找Vj的直接后继Vk，把Vk的入度减一，若Vk入度为0，则入栈
     *      4. 重复上述操作直至栈空为止，若栈空时输出的顶点个数少于图的顶点个数，则图中存在环。
     *
     */
    fun topologicalSort():List<Int>{

        val result= mutableListOf<Int>()
        val stack =Stack<Int>()

        for (i in 0 until vertices){
            if (mInDegrees[i]==0){
                stack.push(i)
            }
        }

        while (stack.isNotEmpty()){

            var vertex=stack.pop()

            //由 vertex指向的点的入度减一
            mAdj[vertex]?.forEach {
                if (--mInDegrees[it]==0){
                    stack.push(it)
                }
            }


            result.add(vertex)


        }


        if (result.size!= vertices){
            throw IllegalStateException("图中有环")
        }

        return result


    }




}