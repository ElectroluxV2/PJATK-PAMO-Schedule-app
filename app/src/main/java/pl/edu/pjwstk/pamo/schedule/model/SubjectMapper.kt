package pl.edu.pjwstk.pamo.schedule.model

import pl.edu.pjwstk.pamo.schedule.scrapper.PjSubject
import pl.edu.pjwstk.pamo.schedule.scrapper.PjSubjectDetailKeys

class SubjectMapper {
    companion object {
        fun toModel(withDetails: Pair<PjSubject, Map<PjSubjectDetailKeys, String>>): PjatkSubject {
            val details = withDetails.second

            val beginTime = details[PjSubjectDetailKeys.BEGIN_TIME]!!
            val endTime = details[PjSubjectDetailKeys.END_TIME]!!

            val interval = "%s - %s".format(
                beginTime.substring(0 , beginTime.lastIndexOf(':')),
                endTime.substring(0 , endTime.lastIndexOf(':'))
            )

            val room = details[PjSubjectDetailKeys.ROOM]!!
            val lecturers = details[PjSubjectDetailKeys.LECTURERS]!!
            val codes = details[PjSubjectDetailKeys.CODES]!!
            val names = details[PjSubjectDetailKeys.NAMES]!!
            val type = details[PjSubjectDetailKeys.TYPE]!!
            val building = details[PjSubjectDetailKeys.BUILDING]!!
            val groups = details[PjSubjectDetailKeys.GROUPS]!!
            val studentsCount = details[PjSubjectDetailKeys.STUDENTS_COUNT]!!

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