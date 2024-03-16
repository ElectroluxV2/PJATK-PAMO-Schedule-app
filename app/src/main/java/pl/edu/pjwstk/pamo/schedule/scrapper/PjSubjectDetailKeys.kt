package pl.edu.pjwstk.pamo.schedule.scrapper

import java.lang.IllegalArgumentException

enum class PjSubjectDetailKeys(val key: String) {
    MS_TEAMS_CODE("KodMsTeams"),
    TYPE("TypZajec"),
    ROOM("Sala"),
    NAMES("NazwaPrzedmioty"),
    DURATION("CzasTrwania"),
    GROUPS("Grupy"),
    DATE("DataZajec"),
    BUILDING("Budynek"),
    STUDENTS_COUNT("LiczbaStudentow"),
    BEGIN_TIME("GodzRozp"),
    END_TIME("GodzZakon"),
    CODES("KodPrzedmiotu"),
    LECTURERS("Dydaktycy");

    companion object {
        fun fromKey(key: String): PjSubjectDetailKeys = when (key) {
            MS_TEAMS_CODE.key -> MS_TEAMS_CODE
            TYPE.key -> TYPE
            ROOM.key -> ROOM
            NAMES.key -> NAMES
            DURATION.key -> DURATION
            GROUPS.key -> GROUPS
            DATE.key -> DATE
            BUILDING.key -> BUILDING
            STUDENTS_COUNT.key -> STUDENTS_COUNT
            BEGIN_TIME.key -> BEGIN_TIME
            END_TIME.key -> END_TIME
            CODES.key -> CODES
            LECTURERS.key -> LECTURERS
            else -> throw IllegalArgumentException()
        }
    }
}
