package pl.edu.pjwstk.pamo.schedule.ui.today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.pjwstk.pamo.schedule.MainActivity
import pl.edu.pjwstk.pamo.schedule.R
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentTodayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val viewModel get() = (activity as MainActivity).viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val subjectRecyclerView: RecyclerView = binding.subjectsRecyclerView

        subjectRecyclerView.addItemDecoration(
            DividerItemDecoration(
                subjectRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subjects.collect {
                    val dateFromArg = arguments?.getString("date")
                    arguments?.clear()
                    val requestedDate = if (dateFromArg == null) LocalDate.now()
                    else LocalDate.parse(dateFromArg, DateTimeFormatter.ISO_LOCAL_DATE)


                    Log.w("OPEN DAY", "%s".format(requestedDate))

                    val subjectsForRequestedDay = it[requestedDate] ?: emptyList()
                    val array = subjectsForRequestedDay.toTypedArray()

                    if (array.isEmpty()) {
                        binding.noData.visibility = View.VISIBLE
                        binding.noData.text = "%s %s".format(
                            getString(R.string.noData), requestedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE))
                    } else {
                        binding.noData.visibility = View.GONE
                        binding.noData.text = ""
                    }

                    subjectRecyclerView.adapter =
                        SubjectsAdapter(array)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}