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

        val json = "[{\"interval\":\"07:30 - 09:00\",\"room\":\"015\",\"lecturers\":\"Adamczyk Maciej\",\"codes\":\"PAU\",\"names\":\"Prawo autorskie\",\"type\":\"Wykład\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.2 - 1w\",\"studentsCount\":\"29 0 ITN\"},{\"interval\":\"08:00 - 09:30\",\"room\":\"B110\",\"lecturers\":\"Zakrzewska Joanna\",\"codes\":\"ESJ\",\"names\":\"Estetyka sztuki Japonii\",\"type\":\"Wykład\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.6 OB.ESJ 1w\",\"studentsCount\":\"16 0 ITN\"},{\"interval\":\"08:30 - 10:00\",\"room\":\"A205-206\",\"lecturers\":\"Idzikowski Tomasz\",\"codes\":\"SOP\",\"names\":\"Systemy operacyjne\",\"type\":\"Wykład\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 1w\",\"studentsCount\":\"57 5 ITN\"},{\"interval\":\"09:00 - 10:30\",\"room\":\"A311\",\"lecturers\":\"Prusinowska Anna\",\"codes\":\"PSEM\",\"names\":\"Proseminarium\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.8 SI 11c\",\"studentsCount\":\"13 2 ITN\"},{\"interval\":\"09:00 - 10:30\",\"room\":\"A405-406\",\"lecturers\":\"Czapiewski Paweł\",\"codes\":\"PAMO\",\"names\":\"Programowanie aplikacji mobilnych\",\"type\":\"Wykład\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.8 AI 1w\",\"studentsCount\":\"37 1 ITN\"},{\"interval\":\"09:15 - 11:30\",\"room\":\"B117\",\"lecturers\":\"Wilczyński Jakub\",\"codes\":\"FOT2\",\"names\":\"Fotografia 2\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.2 - 22c\",\"studentsCount\":\"14 0 ITN\"},{\"interval\":\"09:45 - 11:15\",\"room\":\"B110\",\"lecturers\":\"Zakrzewska Joanna\",\"codes\":\"KAL2\",\"names\":\"Kaligrafia 2\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.2 - 21c\",\"studentsCount\":\"15 0 ITN\"},{\"interval\":\"09:45 - 13:30\",\"room\":\"B207\",\"lecturers\":\"Buszewicz Antonina\",\"codes\":\"ILS1\",\"names\":\"Ilustracja 1\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.6 - 61c\",\"studentsCount\":\"16 0 ITN\"},{\"interval\":\"10:15 - 11:45\",\"room\":\"A205-206\",\"lecturers\":\"Szepietowski Andrzej\",\"codes\":\"MAD1\",\"names\":\"Matematyka dyskretna 1\",\"type\":\"Wykład\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 1w\",\"studentsCount\":\"57 6 ITN\"},{\"interval\":\"10:45 - 12:15\",\"room\":\"A203\",\"lecturers\":\"Obuchowski Aleksander\",\"codes\":\"AAI\",\"names\":\"Zastosowanie sztucznej inteligencji\",\"type\":\"Wykład\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.8 SI 1w\",\"studentsCount\":\"13 1 ITN\"},{\"interval\":\"10:45 - 12:15\",\"room\":\"A310\",\"lecturers\":\"Czapiewski Paweł\",\"codes\":\"PAMO\",\"names\":\"Programowanie aplikacji mobilnych\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.8 AI 11c\",\"studentsCount\":\"18 0 ITN\"},{\"interval\":\"10:45 - 12:15\",\"room\":\"A311\",\"lecturers\":\"Prusinowska Anna\",\"codes\":\"PSEM\",\"names\":\"Proseminarium\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.8 AI 12c\",\"studentsCount\":\"20 0 ITN\"},{\"interval\":\"11:30 - 13:45\",\"room\":\"B117\",\"lecturers\":\"Wilczyński Jakub\",\"codes\":\"FOT2\",\"names\":\"Fotografia 2\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.2 - 21c\",\"studentsCount\":\"15 0 ITN\"},{\"interval\":\"11:45 - 13:15\",\"room\":\"B110\",\"lecturers\":\"Zakrzewska Joanna\",\"codes\":\"KAL2\",\"names\":\"Kaligrafia 2\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk B\",\"groups\":\"GGn I.2 - 22c\",\"studentsCount\":\"14 0 ITN\"},{\"interval\":\"12:00 - 15:00\",\"room\":\"A403\",\"lecturers\":\"Borzyszkowski Tomasz\",\"codes\":\"POJ\",\"names\":\"Programowanie obiektowe w Javie\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 25c\",\"studentsCount\":\"16 0 ITN\"},{\"interval\":\"12:00 - 13:30\",\"room\":\"A410\",\"lecturers\":\"Idzikowski Tomasz\",\"codes\":\"SOP\",\"names\":\"Systemy operacyjne\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 22c\",\"studentsCount\":\"16 0 ITN\"},{\"interval\":\"12:00 - 13:30\",\"room\":\"A502\",\"lecturers\":\"Hyla Michał\",\"codes\":\"WIA2\",\"names\":\"Wstęp do Informatyki i Architektury Komputerów 2\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 23c\",\"studentsCount\":\"12 1 ITN\"},{\"interval\":\"12:00 - 13:30\",\"room\":\"A312\",\"lecturers\":\"Szepietowski Andrzej\",\"codes\":\"MAD1\",\"names\":\"Matematyka dyskretna 1\",\"type\":\"Ćwiczenia\",\"building\":\"Gdańsk A\",\"groups\":\"GIn I.2 - 21c\",\"studentsCount\":\"13 0 ITN\"}]"
        val list = Json.decodeFromString<List<PjatkSubject>>(json)
        viewModel.setSubjects(mapOf(Pair(LocalDate.now(), list)))

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