package com.sirius.miracletools.applist

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sirius.miracletools.Util

class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val _tag = AppListViewModel::class.java.simpleName

    val appList = MutableLiveData<List<ResolveInfo>>()
    val isLoading = MutableLiveData<Boolean>()
    val isLoadError = MutableLiveData<Boolean>()
    private val comparatorOne = AppNameComparator(getApplication())

    fun loadAppList() {
        isLoading.value = true
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        try {
            var resolveInfoList =
                getApplication<Application>().packageManager.queryIntentActivities(mainIntent, 0)
            resolveInfoList.sortWith(comparatorOne)
            appList.value = resolveInfoList
        } catch (e: Exception) {
            isLoadError.value = true
            Log.d(_tag, "loadAppList: ", e)
        } finally {
            isLoadError.value = false
            isLoading.value = false
        }

    }

    class AppNameComparator(val context: Context) : Comparator<ResolveInfo> {
        override fun compare(app1: ResolveInfo?, app2: ResolveInfo?): Int {
            if (app1 == null || app2 == null) {
                return 0;
            }
            val name1 =
                Util.getApplicationName(context, app1.activityInfo.applicationInfo).toString()
            val name2 =
                Util.getApplicationName(context, app2.activityInfo.applicationInfo).toString()

            return name1.compareTo(name2)
        }
    }
}