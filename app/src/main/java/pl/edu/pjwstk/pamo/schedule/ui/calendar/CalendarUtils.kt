package pl.edu.pjwstk.pamo.schedule.ui.calendar

import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder


object CalendarUtils {
    fun getDayBinder(): MonthDayBinder<CalendarViewModel> {
        return object : MonthDayBinder<CalendarViewModel> {
            override fun create(view: View) = CalendarViewModel(view)

            override fun bind(container: CalendarViewModel, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }
    }
}
