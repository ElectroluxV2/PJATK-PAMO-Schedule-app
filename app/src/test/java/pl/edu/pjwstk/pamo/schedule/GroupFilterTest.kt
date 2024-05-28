package pl.edu.pjwstk.pamo.schedule

import org.junit.Test

import org.junit.Assert.*
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel
import java.time.LocalDate

class GroupFilterTest {
    private fun makeSubject(groups: String): PjatkSubject {
        return PjatkSubject("", "", "", "", "", "", "", groups, "")
    }

    @Test
    fun filteringWorks() {
        val subjects = mapOf(LocalDate.now() to listOf(makeSubject("ab"), makeSubject("b"), makeSubject("c")))
        val regex = Regex(".*b.*")

        val filtered = AppViewModel.filterSubjects(subjects, regex)

        assertEquals(2, filtered[LocalDate.now()]?.size)
    }
}