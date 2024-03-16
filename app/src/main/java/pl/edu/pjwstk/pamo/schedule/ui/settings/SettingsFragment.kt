package pl.edu.pjwstk.pamo.schedule.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.serialization.json.Json
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentSettingsBinding
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel
import java.time.LocalDate

class SettingsFragment : Fragment() {
    private val viewModel: AppViewModel by viewModels({ requireActivity() })
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.loadButton.setOnClickListener {
            val constraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workerRequest = OneTimeWorkRequest
                .Builder(FetchWorker::class.java)
                .setConstraints(constraints)
                .build()

            val workManager = WorkManager.getInstance(requireContext())

            workManager.enqueue(workerRequest)

            workManager.getWorkInfoByIdLiveData(workerRequest.id).observe(viewLifecycleOwner) {
                if (!it.state.isFinished) return@observe

                val json = it.outputData.getString("test")!!
                val list = Json.decodeFromString<List<PjatkSubject>>(json)

                viewModel.setSubjects(mapOf(Pair(LocalDate.now(), list)))
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}