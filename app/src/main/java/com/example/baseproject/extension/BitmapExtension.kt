package com.example.baseproject.extension

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toFile(): File {
    val timestamp = System.currentTimeMillis()
    val file = File.createTempFile("profile_picture${timestamp}", ".jpg")
    compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())

    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, bos)
    val bitmapData = bos.toByteArray()

    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return file
}