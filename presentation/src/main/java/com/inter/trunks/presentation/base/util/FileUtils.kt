package com.inter.trunks.presentation.base.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import com.inter.trunks.presentation.base.extensions.toLog
import java.io.File
import java.io.FileInputStream
import java.net.URLConnection.guessContentTypeFromName


fun Uri.getRealPathFromUri(context: Context): String? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) FileUtils.getRealPathFromURI_API19(
        context,
        this
    ) else FileUtils.getRealPathFromURI_BelowAPI19(context, this)

fun Uri.getFileFromUri(context: Context): File? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> FileUtils.getFileFromUri_API29(
            context,
            this
        )
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ->
            FileUtils.getRealPathFromURI_API19(
                context,
                this
            )?.run { File(this) }

        else -> FileUtils.getRealPathFromURI_BelowAPI19(context, this)?.run { File(this) }
    }
}

object FileUtils {

    private const val TAG = "FileUtils"

    fun isFileImage(path: String): Boolean {
        val mimeType = guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }

    fun isFileVideo(path: String): Boolean {
        val mimeType = guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }

    fun getMimeType(path: String): String {
        return guessContentTypeFromName(path)
    }

    fun File.copyTo(file: File) {
        inputStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, projection, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    fun getFileFromUri_API29(context: Context, uri: Uri): File? {
        var file: File? = null
        context.contentResolver.openFileDescriptor(uri, "r", null)?.run {
            val inputStream = FileInputStream(fileDescriptor)
            file = File(context.cacheDir, context.contentResolver.getFilePath(uri))
            inputStream.use { input ->
                file!!.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    fun getRealPathFromURI_BelowAPI19(
        context: Context,
        contentUri: Uri
    ): String? {
        val proj = arrayOf(MediaStore.Video.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        var path: String? = null
        var cursor: Cursor? = null
        try {
            cursor = loader.loadInBackground()!!
            val index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            path = cursor.getString(index)
        } catch (e: Exception) {
            e.toLog(TAG)
        }
        cursor?.close()
        return path
    }

    fun ContentResolver.getFilePath(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
        var cursor: Cursor? = this.query(uri, projection, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                path = cursor.getString(index)
            }
        } catch (e: Exception) {
            e.toLog(TAG)
        } finally {
            cursor?.close()
        }
        return path
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Video.Media.DATA
        var data: String? = null
        val projection = arrayOf(column)
        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                data = cursor.getString(index)
            }
        } catch (e: Exception) {
            e.toLog(TAG)
        } finally {
            cursor?.close()
        }
        return data
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(
        context: Context,
        uri: Uri
    ): String? {
        val isKitKat =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                return if ("primary".equals(type, ignoreCase = true)) {
                    if (split.size > 1) {
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    } else {
                        Environment.getExternalStorageDirectory().toString() + "/"
                    }
                } else {
                    "storage" + "/" + docId.replace(":", "/")
                }
            } else if (isRawDownloadsDocument(uri)) {
                val fileName = context.contentResolver.getFilePath(uri)
                val subFolderName = getSubFolders(uri)
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory()
                        .toString() + "/Download/" + subFolderName + fileName
                }
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isDownloadsDocument(uri)) {
                val fileName = context.contentResolver.getFilePath(uri)
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory()
                        .toString() + "/Download/" + fileName
                }
                var id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:".toRegex(), "")
                    val file = File(id)
                    if (file.exists()) return id
                }
                if (id.startsWith("raw%3A%2F")) {
                    id = id.replaceFirst("raw%3A%2F".toRegex(), "")
                    val file = File(id)
                    if (file.exists()) return id
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getSubFolders(uri: Uri): String {
        val replaceChars = uri.toString()
            .replace("%2F", "/")
            .replace("%20", " ")
            .replace("%3A", ":")
        val bits = replaceChars.split("/").toTypedArray()
        val sub5 = bits[bits.size - 2]
        val sub4 = bits[bits.size - 3]
        val sub3 = bits[bits.size - 4]
        val sub2 = bits[bits.size - 5]
        val sub1 = bits[bits.size - 6]
        return if (sub1 == "Download") {
            "$sub2/$sub3/$sub4/$sub5/"
        } else if (sub2 == "Download") {
            "$sub3/$sub4/$sub5/"
        } else if (sub3 == "Download") {
            "$sub4/$sub5/"
        } else if (sub4 == "Download") {
            "$sub5/"
        } else {
            ""
        }
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean =
        "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri): Boolean =
        "com.android.providers.downloads.documents" == uri.authority

    private fun isRawDownloadsDocument(uri: Uri): Boolean =
        uri.toString().contains("com.android.providers.downloads.documents/document/raw")

    private fun isMediaDocument(uri: Uri): Boolean =
        "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri): Boolean =
        "com.google.android.apps.photos.content" == uri.authority
}