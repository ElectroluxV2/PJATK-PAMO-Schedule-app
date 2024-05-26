package pl.edu.pjwstk.pamo.schedule

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json
import pl.edu.pjwstk.pamo.schedule.databinding.ActivityMainBinding
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.ui.AppViewModel
import java.time.LocalDate

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

        loadData()
    }

    public fun loadData() {
        val sp = applicationContext.getSharedPreferences("test", Context.MODE_PRIVATE)
        val json: String = sp.getString("data", "[]").orEmpty()

        val list = Json.decodeFromString<List<PjatkSubject>>(json)

        Log.w("TEST", "${list.size}")

        viewModel.setSubjects(mapOf(Pair(LocalDate.now(), list)))
    }
}