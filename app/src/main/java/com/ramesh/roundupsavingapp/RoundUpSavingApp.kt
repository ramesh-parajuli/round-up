package com.ramesh.roundupsavingapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//initializes Hilt for dependency injection
@HiltAndroidApp
class RoundUpSavingApp: Application()