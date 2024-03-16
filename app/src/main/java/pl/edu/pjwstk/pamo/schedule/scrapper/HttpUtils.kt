package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

internal object HttpUtils {
    fun makePostRequest(form: Map<String, String>, url: String, userAgent: String): Request.Builder {
        val urlEncodedForm = urlEncodeForm(form)

        val body = urlEncodedForm
            .toByteArray()
            .toRequestBody("application/x-www-form-urlencoded".toMediaType())

        return Request.Builder()
            .url(url)
            .header("user-agent", userAgent)
            .post(body)
    }

    private fun urlEncodeForm(form: Map<String, String>): String {
        return form
            .entries
            .stream()
            .map { field: Map.Entry<String, String> -> URLEncoder.encode(field.key, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(field.value, StandardCharsets.UTF_8) }
            .collect(Collectors.joining("&"))
    }
}
