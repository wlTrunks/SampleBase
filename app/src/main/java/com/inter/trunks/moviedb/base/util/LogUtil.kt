package com.inter.trunks.moviedb.base.util

import android.annotation.SuppressLint
import com.inter.trunks.data.BuildConfig
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


fun Exception.toLogcat(tag: String) {
    if (BuildConfig.DEBUG) {
        println("$tag Begin stacktrace")
        this.printStackTrace()
    }
}

fun String.toLogcat(tag: String) {
    if (BuildConfig.DEBUG) println("$tag: $this")
}


fun writeBytearrayLogFile(byteArray: ByteArray) {
    val logFile = File("/mnt/sdcard/photo1.txt")
    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    } else {
    }
    try {

        val fos = FileOutputStream(logFile)
        fos.write(byteArray)
        fos.close()
//        val fs = FileOutputStream(logFile)
//        val bos = BufferedOutputStream(fs)
        //BufferedWriter for performance, true to set append to file flag
//        val buf = BufferedWriter(FileWriter(logFile, true))
//            buf.append(newLine + "\r\n{" + text + "}\n")
//        bos.write(byteArray)
//        buf.append(text)
//        buf.newLine()
//        bos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    File("/mnt/sdcard/photo2.txt").writeBytes(byteArray)
}

fun writeToFile(text: String, fileName: String = "default.txt") {
    println(text)
    val logFile = File("/mnt/sdcard/${fileName}")
    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    try {
        //BufferedWriter for performance, true to set append to file flag
        val buf = BufferedWriter(FileWriter(logFile))
        buf.write(text)
        buf.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}