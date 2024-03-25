package pl.edu.pjwstk.pamo.schedule.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentCalendarBinding
import pl.edu.pjwstk.pamo.schedule.R
import java.time.YearMonth
import java.util.Locale

class CalendarFragment : Fragment() {
        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView = root.findViewById<CalendarView>(R.id.weekCalendarView)
        val dayViewResource = R.layout.calendar_day_layout

        // calendar config
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
            val firstDayOfWeek = firstDayOfWeekFromLocale()
            calendarView.setup(startDate, endDate, firstDayOfWeek)
            calendarView.scrollToWeek(currentDate)

            val daysOfWeek = daysOfWeek()

            val titlesContainer = root.findViewById<ViewGroup>(R.id.titlesContainer)
            titlesContainer.children
                .map { it as TextView }
                .forEachIndexed { index, textView ->
                    val daysOfWeek
                    val dayOfWeek = daysOfWeek[index]
                    val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    textView.text = title
                }




        calendarView.dayBinder = object : MonthDayBinder<CalendarViewModel> {
            override fun create(view: View) = CalendarViewModel(view)

            override fun bind(container: CalendarViewModel, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

        return root
    }
}