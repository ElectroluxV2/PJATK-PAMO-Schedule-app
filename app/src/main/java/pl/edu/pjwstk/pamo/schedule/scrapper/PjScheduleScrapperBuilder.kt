package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class PjScheduleScrapperBuilder private constructor(
    private val userAgent: String,
    private val httpClient: OkHttpClient
) {
    /** TODO: Add Campus enum with `Warszawa` option  */
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
        @Throws(IOException::class, InterruptedException::class)
        fun forCampus(
            campus: String,
            userAgent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            httpClient: OkHttpClient = OkHttpClient().newBuilder().followRedirects(false).build()
        ): PjScheduleScrapper {
            val builder = PjScheduleScrapperBuilder(userAgent, httpClient)

            // 1. Load default schedule page - currently site redirects to Warsaw by default
            val defaultSchedulePage = builder.getDefaultSchedulePage()
            val baseForm = PjParser.parseHtmlForHiddenInputs(defaultSchedulePage) // Get always returns full page

            // 2. Select campus
            val campusSchedulePage = builder.getSchedulePageForCampus(baseForm, campus)
            val campusForm = PjParser.parseHtmlForHiddenInputs(campusSchedulePage) // Non-ajax post returns full page

            return PjScheduleScrapper(httpClient, userAgent, campusForm)
        }
    }
}
