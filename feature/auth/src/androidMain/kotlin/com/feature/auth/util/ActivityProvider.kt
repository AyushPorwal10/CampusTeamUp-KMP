package com.feature.auth.util

import android.app.Activity
import java.lang.ref.WeakReference

class ActivityProvider {
    private var ref: WeakReference<Activity>? = null

    fun set(activity: Activity) {
        ref = WeakReference(activity)
    }

    fun get(): Activity = ref?.get() ?: error("No active Activity — did you call ActivityProvider.set() in onResume?")
}
