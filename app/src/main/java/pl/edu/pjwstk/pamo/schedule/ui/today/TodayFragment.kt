package pl.edu.pjwstk.pamo.schedule.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentTodayBinding
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel

class TodayFragment: Fragment() {
    private val viewModel: AppViewModel by viewModels({ requireActivity() })
    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val subjectRecyclerView: RecyclerView = binding.subjectsRecyclerView

        subjectRecyclerView.addItemDecoration(DividerItemDecoration(subjectRecyclerView.context, DividerItemDecoration.VERTICAL))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subjectsForSelectedDay.collect {
                    subjectRecyclerView.adapter = SubjectsAdapter(it.toTypedArray())
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