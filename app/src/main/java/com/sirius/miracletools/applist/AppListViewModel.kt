package com.sirius.miracletools.applist

import android.app.Application
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val _tag = AppListViewModel::class.java.simpleName

    val appList = MutableLiveData<List<ResolveInfo>>()
    val isLoading = MutableLiveData<Boolean>()
    val isLoadError = MutableLiveData<Boolean>()

    fun loadAppList() {
        isLoading.value = true
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        try {
            appList.value =
                getApplication<Application>().packageManager.queryIntentActivities(mainIntent, 0)
        } catch (e: Exception) {
            isLoadError.value = true
            Log.d(_tag, "loadAppList: ", e)
        } finally {
            isLoadError.value = false
            isLoading.value = false
        }

    }
}