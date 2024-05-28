package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient
import java.io.IOException

/**
 * Data class representing a PJATK subject.
 *
 * @property eventId The event ID of the subject.
 * @property controlId The control ID of the subject.
 * @property color The color associated with the subject.
 * @property text The text description of the subject.
 * @property httpClient The OkHttpClient instance to use for HTTP requests.
 * @property userAgent The user agent string to include in HTTP requests.
 * @property selectedDateForm The form data for the selected date.
 */
@JvmRecord
data class PjSubject(
    val eventId: String,
    val controlId: String,
    val color: String,
    val text: String,
    val httpClient: OkHttpClient,
    val userAgent: String,
    val selectedDateForm: HashMap<String, String>
) {
    /**
     * Loads the details for the subject by making an HTTP request.
     *
     * @return A map containing the subject details, with keys as [PjSubjectDetailKeys] and values as strings.
     * @throws IOException If an I/O error occurs during the HTTP request.
     * @throws InterruptedException If the thread is interrupted during the request.
     */
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
