/*
 * Copyright (c) 2008-2009, Matthias Mann
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Matthias Mann nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dbpedia.spotlight.util

import org.apache.commons.logging.LogFactory

/**
From: http://www.matthiasmann.de/content/view/25/26/
In nearly every project you have the need to do some simple profiling. Scala makes this very easy and flexible.

Let's assume we load a potentially large XML file:

def loadDataFile(name:String) = ....

Now instead of adding a lot of timing code to your application we can simple do:

 import Profiling._
  val data=timed(printTime("loaded XML in ")){
   loadDataFile(name)
  }

 */
object Profiling {

    val LOG = LogFactory.getLog(this.getClass)

    def timed[T](report: Long=>Unit)(body: =>T) = {
        val start = System.nanoTime
        val r = body
        report(System.nanoTime - start)
        r
    }

    private val timeUnits = List("ns", "us", "ms", "s")
    def formatTime(delta:Long) = {
        def formatTime(v:Long, units:List[String], tail:List[String]):List[String] = {
            def makeTail(what:Long) = (what + units.head) :: tail
            if(!units.tail.isEmpty && v >= 1000)
                formatTime(v / 1000, units.tail, makeTail(v % 1000))
            else
                makeTail(v)
        }
        formatTime(delta, timeUnits, Nil).mkString(" ")
    }
    def printTime(msg:String) = (delta:Long) => {
        LOG.info(msg + formatTime(delta))
    }
}