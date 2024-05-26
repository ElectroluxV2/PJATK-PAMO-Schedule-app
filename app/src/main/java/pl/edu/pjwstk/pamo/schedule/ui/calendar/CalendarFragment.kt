package pl.edu.pjwstk.pamo.schedule.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import pl.edu.pjwstk.pamo.schedule.R
import pl.edu.pjwstk.pamo.schedule.databinding.CalendarDayLayoutBinding
import pl.edu.pjwstk.pamo.schedule.databinding.CalendarHeaderBinding
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()

    private lateinit var binding: FragmentCalendarBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children.forEachIndexed { index, child ->
            (child as TextView).apply {
                text = daysOfWeek[index].name.first().toString()
                setTextColor(resources.getColor(R.color.example_2_white, null))
            }
        }

        configureBinders()
        binding.calendarView.setup(
            YearMonth.now(),
            YearMonth.now().plusMonths(100),
            daysOfWeek.first(),
        )
    }



    private fun configureBinders() {
        val calendarView = binding.calendarView

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).exTwoDayText

            init {
                textView.setOnClickListener {
/*                    if (day.position == DayPosition.MonthDate) {
                        if (selectedDate == day.date) {
                            selectedDate = null
                            calendarView.notifyDayChanged(day)
                        } else {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            calendarView.notifyDateChanged(day.date)
                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
                        }
                        menuItem.isVisible = selectedDate != null
                    }*/
                    Log.w("DAY CLICK", day.date.toString())

                    val bundle = Bundle()
                    bundle.putString("date", day.date.format(DateTimeFormatter.ISO_LOCAL_DATE))

                    Navigation.findNavController(view).navigate(R.id.navigation_today, bundle)
                }
            }
        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.textView
                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) {
                    textView.visibility = View.VISIBLE
                    when (data.date) {
                        selectedDate -> {
                            textView.setTextColor(resources.getColor(R.color.example_2_white, null))
                            textView.setBackgroundResource(R.drawable.example_2_selected_bg)
                        }
                        today -> {
                            textView.setTextColor(resources.getColor(R.color.example_2_red, null))
                            textView.background = null
                        }
                        else -> {
                            textView.setTextColor(resources.getColor(R.color.example_2_black, null))
                            textView.background = null
                        }
                    }
                } else {
                    textView.visibility = View.INVISIBLE
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarHeaderBinding.bind(view).exTwoHeaderText
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                container.textView.text = data.yearMonth.toString()
            }
        }
    }
}
