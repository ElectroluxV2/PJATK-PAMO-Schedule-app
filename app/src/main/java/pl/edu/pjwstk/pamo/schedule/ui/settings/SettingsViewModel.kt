package pl.edu.pjwstk.pamo.schedule.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the SettingsFragment.
 *
 * Provides data and functionality for the Settings UI.
 */
class SettingsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }

    /**
     * LiveData holding the text to be displayed in the settings fragment.
     */
    val text: LiveData<String> = _text
}
