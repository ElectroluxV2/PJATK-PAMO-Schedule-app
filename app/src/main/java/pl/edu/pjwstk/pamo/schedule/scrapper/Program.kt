package pl.edu.pjwstk.pamo.schedule.scrapper

import pl.edu.pjwstk.pamo.schedule.scrapper.PjScheduleScrapperBuilder.Companion.forCampus
import java.io.IOException
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.IntStream

object Program {
    val counter: AtomicInteger = AtomicInteger(0)
    val start: Instant = Instant.now()

    @Throws(IOException::class, InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val scrapper: PjScheduleScrapper = forCampus("Gdańsk")
        val subjects = IntStream
                .rangeClosed(0, 100)
                .mapToObj { x: Int -> LocalDate.now().minusDays(x.toLong()) }
                .parallel()
                .map { x: LocalDate -> loadSubjects(scrapper, x) }
                .flatMap { obj: List<PjSubject?>? -> obj!!.stream() }
                .toList()

        println("Total: " + subjects.size + ", " + Duration.between(start, Instant.now()))
    }

    fun loadSubjects(scrapper: PjScheduleScrapper, date: LocalDate): List<PjSubject> {
        val subjects: List<PjSubject?>?
        try {
            subjects = scrapper.loadSubjects(date)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        for (subject in subjects) {
            println(subject.text + " " + subject.color + " (" + counter.incrementAndGet() + ")")

            val details: Map<PjSubjectDetailKeys, String>
            try {
                details = subject.loadDetails()
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

            for ((key, value) in details) {
                println("-\t$key: $value")
            }
        }

        println(Duration.between(start, Instant.now()))

        return subjects
    }
}
