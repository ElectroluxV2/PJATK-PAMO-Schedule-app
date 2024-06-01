package pl.edu.pjwstk.pamo.schedule.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import pl.edu.pjwstk.pamo.schedule.MainActivity
import pl.edu.pjwstk.pamo.schedule.R
import pl.edu.pjwstk.pamo.schedule.databinding.CalendarDayLayoutBinding
import pl.edu.pjwstk.pamo.schedule.databinding.CalendarHeaderBinding
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * A fragment that displays a calendar view with the ability to highlight specific dates and navigate to detailed views.
 */
class CalendarFragment : Fragment() {
    private val today = LocalDate.now()
    private lateinit var binding: FragmentCalendarBinding
    private val datesToHighlight = mutableListOf<LocalDate>()
    private val viewModel get() = (activity as MainActivity).viewModel

    /**
     * Called to inflate the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called after the fragment's view has been created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val daysOfWeek = daysOfWeek()
        binding.legendLayout.root.children.forEachIndexed { index, child ->
            (child as TextView).apply {
                text = daysOfWeek[(index + 1) % 7].name.first().toString()
                setTextColor(resources.getColor(R.color.white, null))
            }
        }

        configureBinders()
        val currentMonth = YearMonth.now()
        binding.calendarView.setup(
            currentMonth.minusMonths(12),
            YearMonth.now().plusMonths(24),
            daysOfWeek[1],
        )
        binding.calendarView.scrollToMonth(currentMonth)
        collectViewModelData()
    }

    /**
     * Collects data from the ViewModel and updates the calendar view.
     */
    private fun collectViewModelData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subjects.collect { subjects ->
                    arguments?.clear()
                    subjects.forEach { (date, subjectList) ->
                        if (subjectList.isNotEmpty()) {
                            datesToHighlight.add(date)
                        }
                    }

                    // Notify the calendar to refresh
                    binding.calendarView.notifyCalendarChanged()
                }
            }
        }
    }

    /**
     * Configures the binders for the calendar view to handle day and month bindings.
     */
    private fun configureBinders() {
        val calendarView = binding.calendarView

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).exTwoDayText

            init {
                textView.setOnClickListener {
                    Log.w("DAY CLICK", day.date.toString())

                    val bundle = Bundle().apply {
                        putString("date", day.date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    }

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
                        today -> {
                            textView.setTextColor(resources.getColor(R.color.detailLabel, null))
                            textView.setBackgroundResource(R.drawable.selected_bg)
                        }
                        else -> {
                            textView.setTextColor(resources.getColor(com.google.android.material.R.color.material_dynamic_secondary70, null))
                            textView.background = null
                            if (datesToHighlight.contains(data.date)) {
                                textView.setBackgroundResource(R.drawable.selected_lecturers_day_bg) // Assuming you have a drawable for highlighted dates
                            }
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
