package pl.edu.pjwstk.pamo.schedule

import org.junit.Test
import org.junit.Assert.*
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel
import java.time.LocalDate

/**
 * Unit test class for testing the group filtering functionality in [AppViewModel].
 */
class GroupFilterTest {

    /**
     * Helper function to create a [PjatkSubject] with the specified groups.
     *
     * @param groups The groups string to set in the subject.
     * @return A [PjatkSubject] instance with the specified groups.
     */
    private fun makeSubject(groups: String): PjatkSubject {
        return PjatkSubject("", "", "", "", "", "", "", groups, "")
    }

    /**
     * Tests the filtering functionality in [AppViewModel] to ensure it works correctly.
     */
    @Test
    fun filteringWorks() {
        val subjects = mapOf(LocalDate.now() to listOf(makeSubject("ab"), makeSubject("b"), makeSubject("c")))
        val regex = Regex(".*b.*")

        val filtered = AppViewModel.filterSubjects(subjects, regex)

        assertEquals(2, filtered[LocalDate.now()]?.size)
    }
}
