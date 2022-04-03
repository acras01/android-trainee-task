package com.example.android_trainee_task

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ServiceItem internal constructor(var name: String?, var checked: Boolean): Parcelable