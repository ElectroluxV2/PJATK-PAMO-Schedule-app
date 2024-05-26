package pl.edu.pjwstk.pamo.schedule.ui.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.edu.pjwstk.pamo.schedule.model.PjatkSubject
import pl.edu.pjwstk.pamo.schedule.model.SubjectMapper
import pl.edu.pjwstk.pamo.schedule.scrapper.PjScheduleScrapper
import pl.edu.pjwstk.pamo.schedule.scrapper.PjScheduleScrapperBuilder
import java.time.LocalDate
import java.util.Calendar
import java.util.stream.Collectors
import kotlin.time.measureTime


class FetchWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {


        val begin = LocalDate.ofYearDay(2024, 1)
        val end = LocalDate.ofYearDay(2024, 320)

        val forDay = LocalDate.now();

        val scrapper: PjScheduleScrapper = PjScheduleScrapperBuilder.forCampus("Gdańsk")
        Log.i("FetchWorker", "Created scrapper for Gdańsk, loading subjects for %s".format(forDay))

        var mapped: List<PjatkSubject>

        val elapsed = measureTime {
            val subjects = scrapper.loadSubjects(forDay)

            val withDetails = subjects
                .parallelStream()
                .map { Pair(it, it.loadDetails()) }
                .collect(Collectors.toList())

            mapped = withDetails
                .map { SubjectMapper.toModel(it) }
        }

        val sp: SharedPreferences = this.applicationContext.getSharedPreferences("test", MODE_PRIVATE)
        val editor = sp.edit()

        editor.putString("data", Json.encodeToString(mapped))
        editor.apply()

        val output = Data
            .Builder()
            .putString("state", "done")
            .build()

        Log.i("FetchWorker", "Loaded %d subjects in %d seconds".format(mapped.size, elapsed.inWholeSeconds))

        return Result.success(output)
    }
}