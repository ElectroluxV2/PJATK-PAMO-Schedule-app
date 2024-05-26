package pl.edu.pjwstk.pamo.schedule.ui.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import kotlin.time.measureTime


class FetchWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        val logs = MutableLiveData(emptyList<String>())
        val running = MutableLiveData(false)
    }

    override fun doWork(): Result {
        running.postValue(true)
        val from = inputData.getString("from")!!
        val to = inputData.getString("to")!!

        val format = DateTimeFormatter.ISO_LOCAL_DATE

        val fromDate = LocalDate.parse(from, format)
        val toDate = LocalDate.parse(to, format)

        val scrapper: PjScheduleScrapper = PjScheduleScrapperBuilder.forCampus("Gdańsk")
        val entry = "Created scrapper for Gdańsk, loading subjects for %s - %s".format(from, to)
        logs.postValue(logs.value!!.plus(entry))

        Log.i("FetchWorker", entry)

        val sp: SharedPreferences = this.applicationContext.getSharedPreferences("pjpl_events", MODE_PRIVATE)
        val editor = sp.edit()

        var date: LocalDate = fromDate
        while (date.isBefore(toDate)) {
            var mapped: List<PjatkSubject>

            val elapsed = measureTime {
                val subjects = scrapper.loadSubjects(date)

                val withDetails = subjects
                    .parallelStream()
                    .map { Pair(it, it.loadDetails()) }
                    .collect(Collectors.toList())

                mapped = withDetails.mapNotNull { SubjectMapper.toModel(it) }

                editor.putString(date.format(format), Json.encodeToString(mapped))
            }

            val innerEntry = "Loaded %d subjects in %d seconds for date %s".format(mapped.size, elapsed.inWholeSeconds, date)

            logs.postValue(logs.value!!.plus(innerEntry))


            Log.i("FetchWorker", innerEntry)
            date = date.plusDays(1)
        }

        editor.apply()

        running.postValue(false)

        val output = Data
            .Builder()
            .putString("state", "done")
            .build()

        return Result.success(output)
    }
}