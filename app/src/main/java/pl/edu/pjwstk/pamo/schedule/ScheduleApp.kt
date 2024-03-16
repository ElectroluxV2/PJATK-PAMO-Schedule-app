package pl.edu.pjwstk.pamo.schedule

import android.app.Application
import com.google.android.material.color.DynamicColors

class ScheduleApp: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}