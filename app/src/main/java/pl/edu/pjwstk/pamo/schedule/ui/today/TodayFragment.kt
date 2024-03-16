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
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.Orientation
import kotlinx.coroutines.launch
import pl.edu.pjwstk.pamo.schedule.databinding.FragmentTodayBinding

class TodayFragment: Fragment() {
    val viewModel: TodayViewModel by viewModels()
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
                viewModel.uiState.collect {
                    subjectRecyclerView.adapter = SubjectsAdapter(it.subjects.toTypedArray())
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