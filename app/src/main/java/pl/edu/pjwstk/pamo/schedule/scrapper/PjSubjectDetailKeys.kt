package pl.edu.pjwstk.pamo.schedule.scrapper

import java.lang.IllegalArgumentException

/**
 * Enum class representing the keys used for subject details.
 *
 * @property key The string representation of the key.
 */
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
    LECTURERS("Dydaktycy"),
    RESERVATION_TITLE("TytulRezerwacji"),
    RESERVATION_DESCRIPTION("Opis"),
    RESERVATION_AUTHOR("OsobaRezerwujaca"),
    RESERVATION_NAMES("NazwyPrzedmiotow"),
    RESERVATION_CODES("KodyPrzedmiotow"),
    RESERVATION_GROUPS("GrupyStudenckie"),
    RESERVATION_TYPE("TypRezerwacji");

    companion object {
        /**
         * Retrieves the [PjSubjectDetailKeys] corresponding to the given key string.
         *
         * @param key The string representation of the key.
         * @return The corresponding [PjSubjectDetailKeys].
         * @throws IllegalArgumentException If the key does not match any of the enum values.
         */
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
            RESERVATION_TITLE.key -> RESERVATION_TITLE
            RESERVATION_DESCRIPTION.key -> RESERVATION_DESCRIPTION
            RESERVATION_AUTHOR.key -> RESERVATION_AUTHOR
            RESERVATION_TYPE.key -> RESERVATION_TYPE
            RESERVATION_CODES.key -> RESERVATION_CODES
            RESERVATION_NAMES.key -> RESERVATION_NAMES
            RESERVATION_GROUPS.key -> RESERVATION_GROUPS
            else -> throw IllegalArgumentException("Invalid key: %s".format(key))
        }
    }
}
