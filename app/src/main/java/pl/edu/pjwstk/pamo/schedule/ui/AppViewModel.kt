package pl.edu.pjwstk.pamo.schedule.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import java.time.LocalDate

/**
 * ViewModel for managing the application's data and state.
 */
class AppViewModel : ViewModel() {
    private val _subjects = HashMap<LocalDate, List<PjatkSubject>>()
    private val _subjectsLiveData = MutableLiveData(emptyMap<LocalDate, List<PjatkSubject>>())
    val subjects = _subjectsLiveData.asFlow()

    private var _groupsRegex = Regex(".*")

    /**
     * Sets the subjects and updates the filtered subjects.
     *
     * @param subjects A map of dates to lists of subjects.
     */
    fun setSubjects(subjects: Map<LocalDate, List<PjatkSubject>>) {
        _subjects.clear()
        _subjects.putAll(subjects)

        recalculateSubjectsFilter()
    }

    /**
     * Sets the regex for filtering groups and updates the filtered subjects.
     *
     * @param regex The regex string to filter groups.
     */
    fun setGroupsRegex(regex: String) {
        _groupsRegex = Regex(regex)

        recalculateSubjectsFilter()
    }

    /**
     * Recalculates the filtered subjects based on the current subjects and groups regex.
     */
    private fun recalculateSubjectsFilter() {
        val recalculated = filterSubjects(_subjects, _groupsRegex)

        _subjectsLiveData.postValue(recalculated)
    }

    companion object {
        /**
         * Filters the subjects based on the provided regex.
         *
         * @param subjects A map of dates to lists of subjects.
         * @param regex The regex to filter groups.
         * @return A map of filtered subjects.
         */
        fun filterSubjects(subjects: Map<LocalDate, List<PjatkSubject>>, regex: Regex): Map<LocalDate, List<PjatkSubject>> {
            return subjects.mapValues { entry -> entry.value.filter { regex.matches(it.groups) } }
        }
    }
}
