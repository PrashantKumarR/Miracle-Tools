package com.sirius.miracletools.permissionlist

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sirius.miracletools.applist.AppListViewModel

class PermissionListViewModel(application: Application) : AndroidViewModel(application) {
    private val _tag = PermissionListViewModel::class.java.simpleName

    val permissionsList = MutableLiveData<MutableMap<String, MutableList<ApplicationInfo>>>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()
    private val comparatorOne = AppListViewModel.AppNameComparator(getApplication())

    fun refresh() {
        isLoading.value = true
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val permissionMap = mutableMapOf<String, MutableList<ApplicationInfo>>()
        try {
            var resolveInfoList =
                getApplication<Application>().packageManager.queryIntentActivities(mainIntent, 0)
            resolveInfoList.sortWith(comparatorOne)
            for (app in resolveInfoList.iterator()) {
                try {
                    val packageInfo = getApplication<Application>().packageManager.getPackageInfo(
                        app.activityInfo.applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS
                    )
                    packageInfo.requestedPermissions?.let {
                        for (permission: String in packageInfo.requestedPermissions) {
                            try {
                                if (permission.startsWith("android.permission.")) {
                                    var appList = permissionMap[permission] ?: mutableListOf()
                                    appList.add(packageInfo.applicationInfo)
                                    permissionMap[permission] = appList
                                }
                            } catch (e: Exception) {
                                isError.value = true
                                Log.d(_tag, "loadAppList: ", e)
                            }
                        }
                    }
                } catch (e: Exception) {
                    isError.value = true
                    Log.d(_tag, "loadAppList: ", e)
                }
            }
        } catch (e: Exception) {
            isError.value = true
            Log.d(_tag, "loadAppList: ", e)
        } finally {
            isError.value = false
            isLoading.value = false
        }
        permissionsList.value = permissionMap
    }
}
