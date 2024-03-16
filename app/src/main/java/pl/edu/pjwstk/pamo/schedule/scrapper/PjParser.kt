package pl.edu.pjwstk.pamo.schedule.scrapper

import okhttp3.OkHttpClient


internal object PjParser {
    fun parseHtmlForSubjects(html: String, httpClient: OkHttpClient, userAgent: String, selectedDateForm: HashMap<String, String>): List<PjSubject> {
        val subjects = ArrayList<PjSubject>()

        var pivot = 0
        while (pivot < html.length) {
            // 1. Check for div with id

            val blockBegin = "<div id=\""
            val indexOfBlockBegin = html.indexOf(blockBegin, pivot)
            if (indexOfBlockBegin == -1) break
            pivot = indexOfBlockBegin + blockBegin.length
            val divEndIndex = html.indexOf(">", pivot)

            // 2. Extract value of id
            val indexOfEndOfIdAttribute = html.indexOf("\"", pivot)
            if (indexOfEndOfIdAttribute == -1 || indexOfEndOfIdAttribute > divEndIndex) break
            val id = html.substring(pivot, indexOfEndOfIdAttribute)
            pivot = indexOfEndOfIdAttribute

            // 3. Extract title
            val titleAttributeBegin = "title=\""
            val indexOfTitleAttributeBegin = html.indexOf(titleAttributeBegin, pivot)
            if (indexOfTitleAttributeBegin == -1 || indexOfTitleAttributeBegin > divEndIndex) continue  // .rsAptContent mush have style attribute

            pivot = indexOfTitleAttributeBegin + titleAttributeBegin.length
            val endOfTitleAttribute = html.indexOf("\"", pivot)
            val title = html.substring(pivot, endOfTitleAttribute)
            pivot = endOfTitleAttribute

            // 4. Check for class attribute
            val classAttributeBegin = "class=\""
            val indexOfClassAttributeBegin = html.indexOf(classAttributeBegin, pivot)
            if (indexOfClassAttributeBegin == -1 || indexOfClassAttributeBegin > divEndIndex) break
            pivot = indexOfClassAttributeBegin + classAttributeBegin.length
            val indexOfClassAttributeEnd = html.indexOf("\"", pivot)

            // 5. Class must be 'rsAptSubject'
            val classList = html.substring(pivot, indexOfClassAttributeEnd)
            if (!classList.contains("rsAptSubject")) continue  // div with id and class, but not subject

            pivot = indexOfClassAttributeEnd

            // 6. Find .rsAptContent
            val rsAptContentBegin = "class=\"rsAptContent"
            val indexOfRsAptContentBegin = html.indexOf(rsAptContentBegin, pivot)
            if (indexOfRsAptContentBegin == -1) continue  // subject shall contain .rsAptContent block

            pivot = indexOfRsAptContentBegin + rsAptContentBegin.length

            // 7. Extract style value from .rsAptContent
            val styleAttributeBegin = "style=\""
            val indexOfStyleAttributeBegin = html.indexOf(styleAttributeBegin, pivot)
            if (indexOfStyleAttributeBegin == -1) continue  // .rsAptContent mush have style attribute

            pivot = indexOfStyleAttributeBegin + styleAttributeBegin.length
            val endOfStyleAttribute = html.indexOf("\"", pivot)
            val style = html.substring(pivot, endOfStyleAttribute)
            pivot = endOfStyleAttribute

            // 8. Extract background color value
            val backgroundColorBegin = "background-color:"
            val indexOfBackgroundColorBegin = style.indexOf(backgroundColorBegin)
            if (indexOfBackgroundColorBegin == -1) continue  // Every subject has color assigned

            val backgroundColorBeginIndex = indexOfBackgroundColorBegin + backgroundColorBegin.length
            val indexOfBackgroundColorEnd = style.indexOf(";", backgroundColorBeginIndex)
            val backgroundColor = style.substring(backgroundColorBeginIndex, indexOfBackgroundColorEnd)

            // 9. Extract text
            val textBegin = "cell\">"
            val indexOfTextBegin = html.indexOf(textBegin, pivot)
            if (indexOfTextBegin == -1) continue  // Every subject has .center-cell text

            pivot = indexOfTextBegin + textBegin.length
            val textEndIndex = html.indexOf("</", pivot)
            val text = html.substring(pivot, textEndIndex).trim { it <= ' ' }
            pivot = textEndIndex

            // 10. Parse title for eventId
            val eventId = title.substring(0, title.length - 1)

            // Finally consume scrapped data
            val subject = PjSubject(eventId, id, backgroundColor, text, httpClient, userAgent, selectedDateForm)
            subjects.add(subject)
        }

        return subjects
    }

    fun parsePopoutForSubjectDetails(popout: String): Map<PjSubjectDetailKeys, String> {
        val details = HashMap<PjSubjectDetailKeys, String>()
        var pivot = 0

        while (pivot < popout.length) {
            val spanBegin = "<span"
            val indexOfSpanBegin = popout.indexOf(spanBegin, pivot)
            if (indexOfSpanBegin == -1) break
            pivot = indexOfSpanBegin + spanBegin.length


            val idBegin = "id=\""
            val indexOfIdBegin = popout.indexOf(idBegin, pivot)
            if (indexOfIdBegin == -1) break
            pivot = indexOfIdBegin + idBegin.length
            val indexOfIdEnd = popout.indexOf("\"", pivot)
            val id = popout.substring(pivot, indexOfIdEnd)
            pivot = indexOfIdEnd

            val indexOfTextBegin = popout.indexOf(">", pivot) + 1
            val indexOfTextEnd = popout.indexOf("</", indexOfTextBegin)
            val text = popout.substring(indexOfTextBegin, indexOfTextEnd)
            pivot = indexOfTextEnd

            val key = id.substring(id.lastIndexOf("_") + 1, id.lastIndexOf("Label"))
            val value = text.trim { it <= ' ' }

            val enumKey = PjSubjectDetailKeys.fromKey(key)
            details[enumKey] = value
        }

        return details
    }

    fun parseHtmlForHiddenInputs(html: String): HashMap<String, String> {
        val inputs = HashMap<String, String>()
        var pivot = 0

        while (pivot < html.length) {
            val hiddenInputBegin = "<input type=\"hidden\""
            val indexOfHiddenInput = html.indexOf(hiddenInputBegin, pivot)
            if (indexOfHiddenInput == -1) break // No more inputs to parse


            pivot = indexOfHiddenInput

            val inputNameAttributeBegin = "name=\""

            val indexOfNameAttributeBegin = html.indexOf(inputNameAttributeBegin, pivot)
            if (indexOfNameAttributeBegin == -1) continue
            val beginIndexOfNameAttribute = indexOfNameAttributeBegin + inputNameAttributeBegin.length
            val endIndexOfNameAttribute = html.indexOf("\"", beginIndexOfNameAttribute)

            val inputName = html.substring(beginIndexOfNameAttribute, endIndexOfNameAttribute)
            pivot = endIndexOfNameAttribute

            val inputValueAttributeBegin = "value=\""

            val indexOfValueAttributeBegin = html.indexOf(inputValueAttributeBegin, endIndexOfNameAttribute)
            if (indexOfValueAttributeBegin == -1) continue
            val beginIndexOfValueAttribute = indexOfValueAttributeBegin + inputValueAttributeBegin.length
            val endIndexOfValueAttribute = html.indexOf("\"", beginIndexOfValueAttribute)

            val inputValue = html.substring(beginIndexOfValueAttribute, endIndexOfValueAttribute)
            pivot = endIndexOfValueAttribute

            inputs[inputName] = inputValue
        }

        return inputs
    }
}
