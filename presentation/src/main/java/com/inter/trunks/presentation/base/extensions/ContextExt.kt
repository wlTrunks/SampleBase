/*
 * Copyright (c) 2019, TeamDirector LLC. All rights reserved.
 */

package com.inter.trunks.presentation.base.extensions

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Get View
 * @param idRes layout id
 */
fun Context.getView(@LayoutRes idRes: Int, root: ViewGroup? = null, attachToRoot: Boolean = false) =
    LayoutInflater.from(this).inflate(idRes, root, attachToRoot)

/**
 * Checking if service runnig
 */
fun <T : Service> Context.isServiceRunning(serviceClass: Class<T>): Boolean {
    val manager: ActivityManager =
        this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return manager.getRunningServices(Integer.MAX_VALUE).filter { s ->
        s.service.className == serviceClass.name
    }.isNotEmpty()
}
