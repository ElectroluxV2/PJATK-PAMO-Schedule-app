package pl.edu.pjwstk.pamo.schedule.model

import pl.edu.pjwstk.pamo.schedule.scrapper.PjSubject
import pl.edu.pjwstk.pamo.schedule.scrapper.PjSubjectDetailKeys

/**
 * Object responsible for mapping details to a PjatkSubject model.
 */
class SubjectMapper {
    companion object {
        /**
         * Converts a pair of PjSubject and a map of PjSubjectDetailKeys to a PjatkSubject.
         *
         * @param withDetails A pair containing a PjSubject and a map of details where keys are of type PjSubjectDetailKeys and values are strings.
         * @return A PjatkSubject object if all required details are present, otherwise null.
         */
        fun toModel(withDetails: Pair<PjSubject, Map<PjSubjectDetailKeys, String>>): PjatkSubject? {
            // Extracting the details map from the pair
            val details = withDetails.second

            // Extracting and formatting the begin and end times
            val beginTime = details[PjSubjectDetailKeys.BEGIN_TIME]!!
            val endTime = details[PjSubjectDetailKeys.END_TIME]!!

            val interval = "%s - %s".format(
                beginTime.substring(0, beginTime.lastIndexOf(':')),
                endTime.substring(0, endTime.lastIndexOf(':'))
            )

            // Extracting other details from the map
            val room = details[PjSubjectDetailKeys.ROOM]
            val lecturers = details[PjSubjectDetailKeys.LECTURERS]
            val codes = details[PjSubjectDetailKeys.CODES]
            val names = details[PjSubjectDetailKeys.NAMES]
            val type = details[PjSubjectDetailKeys.TYPE]
            val building = details[PjSubjectDetailKeys.BUILDING]
            val groups = details[PjSubjectDetailKeys.GROUPS]
            val studentsCount = details[PjSubjectDetailKeys.STUDENTS_COUNT]

            // Checking if any of the required details are null
            if (room == null || lecturers == null || codes == null || names == null || type == null || building == null || groups == null || studentsCount == null) {
                return null
            }

            // Returning a new PjatkSubject object with the extracted details
            return PjatkSubject(
                interval,
                room,
                lecturers,
                codes,
                names,
                type,
                building,
                groups,
                studentsCount
            )
        }
    }
}
