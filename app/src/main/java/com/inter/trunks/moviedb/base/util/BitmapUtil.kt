package com.inter.trunks.moviedb.base.util

import android.content.Context
import android.graphics.Bitmap
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import android.R
import android.graphics.BitmapFactory
import java.io.*


const val PHOTO_DIR = "photoDir"

fun Bitmap.saveToInternalStorage(name: String, context: Context): String {
    val directory = ContextWrapper(context).getDir(PHOTO_DIR, MODE_PRIVATE)
    val mypath = File(directory, name)
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(mypath)
        this.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return mypath.absolutePath
}

fun loadImageFromInternal(name: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val file = File(name)
        bitmap = BitmapFactory.decodeStream(FileInputStream(file))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}