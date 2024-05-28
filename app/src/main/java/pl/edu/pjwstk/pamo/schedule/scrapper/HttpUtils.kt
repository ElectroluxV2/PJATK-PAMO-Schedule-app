package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

/**
 * Utility object for making HTTP requests.
 */
internal object HttpUtils {
    /**
     * Creates a POST request with the given form data, URL, and user agent.
     *
     * @param form The form data to be included in the POST request.
     * @param url The URL to which the request is sent.
     * @param userAgent The user agent to be included in the request header.
     * @return A [Request.Builder] object configured with the given parameters.
     */
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

    /**
     * URL-encodes the given form data.
     *
     * @param form The form data to be URL-encoded.
     * @return A string representing the URL-encoded form data.
     */
    private fun urlEncodeForm(form: Map<String, String>): String {
        return form
            .entries
            .stream()
            .map { field: Map.Entry<String, String> ->
                URLEncoder.encode(field.key, StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(field.value, StandardCharsets.UTF_8)
            }
            .collect(Collectors.joining("&"))
    }
}
