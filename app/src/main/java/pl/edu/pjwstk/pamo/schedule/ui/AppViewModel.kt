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

    fun setSubjects(subjects: Map<LocalDate, List<PjatkSubject>>) {
        _subjects.clear();
        _subjects.putAll(subjects);

        _subjectsForSelectedDay.value = _subjects[LocalDate.now()]
    }
}