package pl.edu.pjwstk.pamo.schedule

import android.app.Application
import com.google.android.material.color.DynamicColors

/**
 * Application class for the schedule app.
 *
 * Initializes dynamic colors for activities if available.
 */
class ScheduleApp: Application() {
    /**
     * Called when the application is starting, before any activity, service, or receiver objects (excluding content providers) have been created.
     */
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
