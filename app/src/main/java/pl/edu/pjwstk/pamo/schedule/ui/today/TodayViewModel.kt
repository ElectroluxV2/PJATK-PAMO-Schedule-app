package pl.edu.pjwstk.pamo.schedule.ui.today

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject

val subjects = generateSequence { PjatkSubject(
    "08:15 - 09:45",
    "A405-406",
    "Radomski Autur",
    "SBD",
    "Systemy baz danych",
    "ćwiczenia",
    "Gdańsk A",
    "Gln l.6 - 604l",
    "51 0 ITN"
) }.take(30).toList()

data class TodayUiState(
    val text: String = "This is today Fragment",
    val subjects: List<PjatkSubject> = emptyList(),
)

class TodayViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodayUiState(subjects = subjects))
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    companion object {
        var test = 0
    }

    fun loadSubjects() {
       _uiState.update { currentState -> currentState.copy(
           text = currentState.text + " " + test++,
           subjects = subjects,
       ) }
    }
}