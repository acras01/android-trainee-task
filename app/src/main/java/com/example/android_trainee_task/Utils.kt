package com.example.android_trainee_task

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import androidx.core.hardware.display.DisplayManagerCompat
import java.io.IOException

fun getObjectsFromJson(context: Context): String {

    val jsonString: String = try {
        context.assets.open("pins.json").bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        ""
    }

    return jsonString
}

fun screenValue(activity: Activity): Array<Int> {
    val height: Int
    val width: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        width = activity.resources.displayMetrics.widthPixels
        height = activity.resources.displayMetrics.heightPixels
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }
    return arrayOf(height, width)
}


