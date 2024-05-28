package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient
import java.io.IOException
import java.time.LocalDate

/**
 * A class responsible for scraping the PJATK schedule for subjects.
 *
 * @property httpClient The OkHttpClient instance to use for HTTP requests.
 * @property userAgent The user agent string to include in HTTP requests.
 * @property campusForm The base form data required for making requests to the schedule page.
 */
class PjScheduleScrapper internal constructor(
    private val httpClient: OkHttpClient,
    private val userAgent: String,
    private val campusForm: HashMap<String, String>
) {
    companion object {
        const val SCHEDULE_PAGE_URL = "https://planzajec.pjwstk.edu.pl/PlanOgolny.aspx"
    }

    /**
     * Loads the subjects for a given date by scraping the schedule page.
     *
     * @param date The date for which to load the subjects.
     * @return A list of [PjSubject] objects representing the subjects on the given date.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the request.
     */
    @Throws(IOException::class, InterruptedException::class)
    fun loadSubjects(date: LocalDate): List<PjSubject> {
        // 1. Select date
        val selectedDatePage = getSchedulePageForDate(campusForm, date)
        val selectedDateForm = PjParser.parseHtmlForHiddenInputs(selectedDatePage) // Non-ajax post returns full page

        // 2. List all subjects for given date
        return PjParser.parseHtmlForSubjects(selectedDatePage, httpClient, userAgent, selectedDateForm)
    }

    /**
     * Retrieves the schedule page HTML for a given date by making a POST request.
     *
     * @param baseForm The base form data to be included in the POST request.
     * @param date The date for which to retrieve the schedule page.
     * @return The HTML content of the schedule page for the given date.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the request.
     */
    @Throws(IOException::class, InterruptedException::class)
    private fun getSchedulePageForDate(baseForm: HashMap<String, String>, date: LocalDate): String {
        val dayAfterDate = date.plusDays(1)
        val formWithDateSelection = HashMap(baseForm)

        formWithDateSelection["ScriptManager1"] = "RadAjaxPanel1Panel|PlanZajecRadScheduler\$SelectedDateCalendar"
        formWithDateSelection["__EVENTTARGET"] = "PlanZajecRadScheduler\$SelectedDateCalendar"
        formWithDateSelection["__EVENTARGUMENT"] = "d"
        formWithDateSelection["PlanZajecRadScheduler_SelectedDateCalendar_SD"] = "[[%d,%d,%d]]".format(date.year, date.monthValue, date.dayOfMonth)
        formWithDateSelection["PlanZajecRadScheduler_SelectedDateCalendar_AD"] = "[[1900,1,1],[2099,12,30],[%d,%d,%d]]".format(dayAfterDate.year, dayAfterDate.monthValue, dayAfterDate.dayOfMonth)

        val submitRequest = HttpUtils
            .makePostRequest(formWithDateSelection, SCHEDULE_PAGE_URL, userAgent)
            .build()

        val response = httpClient
            .newCall(submitRequest)
            .execute()
            .body

        return requireNotNull(response).string()
    }
}
