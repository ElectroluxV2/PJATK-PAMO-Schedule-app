package pl.edu.pjwstk.pamo.schedule.ui.settings

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import pl.edu.pjwstk.pamo.schedule.MainActivity
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentSettingsBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * A fragment that provides settings functionality for the application, including date selection and data fetching.
 */
class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels({ requireActivity() })
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Called to inflate the fragment's view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.dateFrom.transformIntoDatePicker(requireContext(), LocalDate.now())
        binding.dateTo.transformIntoDatePicker(requireContext(), LocalDate.now().plusDays(30))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                FetchWorker.running.asFlow().collect {
                    binding.loadButton.isEnabled = !it
                    binding.progressIndicator.isIndeterminate = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                FetchWorker.logs.asFlow().collect {
                    binding.logs.text = it.joinToString("\n")
                }
            }
        }

        val sp = requireContext().getSharedPreferences("pjpl_settings", Context.MODE_PRIVATE)

        binding.groups.doAfterTextChanged {
            try {
                (activity as MainActivity).viewModel.setGroupsRegex(it.toString())
                sp.edit().putString("regex", it.toString()).apply()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Invalid regex: %s".format(e.message), Snackbar.LENGTH_LONG).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.groups.setText(sp.getString("regex", null) ?: ".*")
            }
        }

        binding.loadButton.setOnClickListener {
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workerRequest = OneTimeWorkRequest
                .Builder(FetchWorker::class.java)
                .setInputData(Data
                    .Builder()
                    .putString("from", binding.dateFrom.text.toString())
                    .putString("to", binding.dateTo.text.toString())
                    .build()
                )
                .setConstraints(constraints)
                .build()

            val workManager = WorkManager.getInstance(requireContext())

            workManager.enqueueUniqueWork("scrapper", ExistingWorkPolicy.REPLACE, workerRequest)
            binding.progressIndicator.indeterminateAnimationType = LinearProgressIndicator.INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS

            workManager.getWorkInfoByIdLiveData(workerRequest.id).observe(viewLifecycleOwner) {
                if (!it.state.isFinished) return@observe

                (activity as MainActivity).loadData()
            }
        }

        return root
    }

    /**
     * Called when the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Extension function to transform an EditText into a date picker.
     *
     * @param context The context in which the date picker is shown.
     * @param value The initial date to display in the date picker.
     */
    fun EditText.transformIntoDatePicker(context: Context, value: LocalDate) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false
        val format = DateTimeFormatter.ISO_LOCAL_DATE
        setText(value.format(format))

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setText(LocalDateTime.from(myCalendar.time.toInstant().atZone(ZoneId.systemDefault())).format(format))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                value.also { datePicker.updateDate(it.year, it.monthValue, it.dayOfMonth) }
                show()
            }
        }
    }
}
