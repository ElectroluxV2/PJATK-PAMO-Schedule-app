package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient
import java.io.IOException

@JvmRecord
data class PjSubject(val eventId: String, val controlId: String, val color: String, val text: String, val httpClient: OkHttpClient, val userAgent: String, val selectedDateForm: HashMap<String, String>) {
    @Throws(IOException::class, InterruptedException::class)
    fun loadDetails(): Map<PjSubjectDetailKeys, String> {
        val formWithSubjectSelection = HashMap(selectedDateForm)

        formWithSubjectSelection["ScriptManager1"] = "RadToolTipManager1RTMPanel|RadToolTipManager1RTMPanel"
        formWithSubjectSelection["__EVENTTARGET"] = "RadToolTipManager1RTMPanel"
        formWithSubjectSelection["RadToolTipManager1_ClientState"] = "{\"AjaxTargetControl\":\"%s\",\"Value\":\"%s\"}".format(controlId, eventId)


        val submitRequest = HttpUtils.makePostRequest(formWithSubjectSelection, PjScheduleScrapper.SCHEDULE_PAGE_URL, userAgent)
            .header("x-microsoftajax", "Delta=true")
            .build()

        val body = httpClient
            .newCall(submitRequest)
            .execute()
            .body

        val popout = requireNotNull(body).string()

        return PjParser.parsePopoutForSubjectDetails(popout)
    }
}
