package com.inter.trunks.presentation.base.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager


@SuppressLint("MissingPermission")
fun Context.isNetworkConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}