package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * A builder class for creating [PjScheduleScrapper] instances configured for a specific campus.
 *
 * @property userAgent The user agent string to include in HTTP requests.
 * @property httpClient The OkHttpClient instance to use for HTTP requests.
 */
class PjScheduleScrapperBuilder private constructor(
    private val userAgent: String,
    private val httpClient: OkHttpClient
) {
    /**
     * Retrieves the schedule page for a specific campus by making a POST request.
     *
     * @param baseForm The base form data to be included in the POST request.
     * @param campus The name of the campus to select.
     * @return The HTML content of the schedule page for the selected campus.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the request.
     */
    @Throws(IOException::class, InterruptedException::class)
    private fun getSchedulePageForCampus(
        baseForm: HashMap<String, String>,
        campus: String
    ): String {
        val formWithCampusSelection = HashMap(baseForm)

        // Emulate JS click - Change campus to Gdańsk
        formWithCampusSelection["ScriptManager1"] = "RadAjaxPanel1Panel|WydzialComboBox"
        formWithCampusSelection["__EVENTTARGET"] = "WydzialComboBox"
        formWithCampusSelection["__EVENTARGUMENT"] = "{\"Command\":\"Select\",\"Index\":1}"
        formWithCampusSelection["WydzialComboBox"] = "Gdańsk"
        formWithCampusSelection["WydzialComboBox_ClientState"] =
            "{\"logEntries\":[],\"value\":\"2\",\"text\":\"Gdańsk\",\"enabled\":true,\"checkedIndices\":[],\"checkedItemsTextOverflows\":false}"

        val submitRequest = HttpUtils.makePostRequest(
            formWithCampusSelection,
            PjScheduleScrapper.SCHEDULE_PAGE_URL,
            userAgent
        ).build()

        val response = httpClient
            .newCall(submitRequest)
            .execute()
            .body

        return requireNotNull(response).string()
    }

    /**
     * Retrieves the default schedule page by making a GET request.
     *
     * @return The HTML content of the default schedule page.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the request.
     */
    @Throws(IOException::class, InterruptedException::class)
    private fun getDefaultSchedulePage(): String {
        val request = Request
            .Builder()
            .url(PjScheduleScrapper.SCHEDULE_PAGE_URL)
            .header("user-agent", userAgent)
            .get()
            .build()

        val response = httpClient
            .newCall(request)
            .execute()
            .body

        return requireNotNull(response).string()
    }

    companion object {
        /**
         * Creates a [PjScheduleScrapper] instance for the specified campus.
         *
         * @param campus The name of the campus for which to create the scrapper.
         * @param userAgent The user agent string to include in HTTP requests.
         * @param httpClient The OkHttpClient instance to use for HTTP requests.
         * @return A [PjScheduleScrapper] instance configured for the specified campus.
         * @throws IOException If an I/O error occurs during the HTTP request.
         * @throws InterruptedException If the thread is interrupted during the request.
         */
        @Throws(IOException::class, InterruptedException::class)
        fun forCampus(
            campus: String,
            userAgent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            httpClient: OkHttpClient = OkHttpClient().newBuilder().followRedirects(false).build()
        ): PjScheduleScrapper {
            val builder = PjScheduleScrapperBuilder(userAgent, httpClient)

            // 1. Load default schedule page - currently site redirects to Warsaw by default
            val defaultSchedulePage = builder.getDefaultSchedulePage()
            val baseForm = PjParser.parseHtmlForHiddenInputs(defaultSchedulePage) // GET always returns full page

            // 2. Select campus
            val campusSchedulePage = builder.getSchedulePageForCampus(baseForm, campus)
            val campusForm = PjParser.parseHtmlForHiddenInputs(campusSchedulePage) // Non-ajax post returns full page

            return PjScheduleScrapper(httpClient, userAgent, campusForm)
        }
    }
}
