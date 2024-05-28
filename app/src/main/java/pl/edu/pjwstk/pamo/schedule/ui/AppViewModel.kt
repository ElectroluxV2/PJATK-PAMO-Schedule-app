package pl.edu.pjwstk.pamo.schedule.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import java.time.LocalDate

class AppViewModel : ViewModel() {
    private val _subjects = HashMap<LocalDate, List<PjatkSubject>>()
    private val _subjectsLiveData = MutableLiveData(emptyMap<LocalDate, List<PjatkSubject>>())
    val subjects = _subjectsLiveData.asFlow()

    private var _groupsRegex = Regex(".*")

    fun setSubjects(subjects: Map<LocalDate, List<PjatkSubject>>) {
        _subjects.clear()
        _subjects.putAll(subjects)

        recalculateSubjectsFilter()
    }

    fun setGroupsRegex(regex: String) {
        _groupsRegex = Regex(regex)

        recalculateSubjectsFilter()
    }

    private fun recalculateSubjectsFilter() {
        val recalculated = filterSubjects(_subjects, _groupsRegex)

        _subjectsLiveData.postValue(recalculated)
    }

    companion object {
        fun filterSubjects(subjects: Map<LocalDate, List<PjatkSubject>>, regex: Regex): Map<LocalDate, List<PjatkSubject>> {
            return subjects.mapValues { entry -> entry.value.filter { regex.matches(it.groups) } }
        }
    }
}