package pl.edu.pjwstk.pamo.schedule

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import pl.edu.pjwstk.pamo.schedule.databinding.ActivityMainBinding
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!
        val navController = navHostFragment.findNavController()

        navView.setupWithNavController(navController)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loadData()
            }
        }
    }

    public fun loadData() {
        val format = DateTimeFormatter.ISO_LOCAL_DATE
        val sp = applicationContext.getSharedPreferences("pjpl_events", Context.MODE_PRIVATE)

        val subjectsMap = sp.all.entries
            .filter { it.key != null && it.value != null && it.key is String && it.value is String }
            .associateBy({ LocalDate.parse(it.key, format) }, { Json.decodeFromString<List<PjatkSubject>>(it.value as String) })

        Log.w("DATA LOAD", "Loaded ${subjectsMap.size} days from SharedPreferences")

        viewModel.setSubjects(subjectsMap)
    }
}