package pl.edu.pjwstk.pamo.schedule.ui.calendar

import android.view.View
import android.widget.TextView
import pl.edu.pjwstk.pamo.schedule.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer

class CalendarViewModel(view: View) : ViewContainer(view) {

    val textView = view.findViewById<TextView>(R.id.calendarDayText)


}