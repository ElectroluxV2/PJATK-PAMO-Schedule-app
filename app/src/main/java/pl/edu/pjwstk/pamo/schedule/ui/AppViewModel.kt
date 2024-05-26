package pl.edu.pjwstk.pamo.schedule.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import java.time.LocalDate

class AppViewModel : ViewModel() {
    private val _subjects = HashMap<LocalDate, List<PjatkSubject>>()
    private val _subjectsForSelectedDay = MutableLiveData(emptyList<PjatkSubject>())
    val subjectsForSelectedDay = _subjectsForSelectedDay.asFlow()

    private var groupsRegex = Regex(".*")

    fun setSubjects(subjects: Map<LocalDate, List<PjatkSubject>>) {
        _subjects.clear();
        _subjects.putAll(subjects);

        calculateTodaySubjects()
    }

    fun setGroupsRegex(regex: String) {
        groupsRegex = Regex(regex)

        calculateTodaySubjects()
    }

    private fun calculateTodaySubjects() {
        _subjectsForSelectedDay.value = _subjects[LocalDate.now()]
            .orEmpty()
            .filter { groupsRegex.matches(it.groups) }
    }
}