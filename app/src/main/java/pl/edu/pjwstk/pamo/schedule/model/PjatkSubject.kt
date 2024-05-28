package pl.edu.pjwstk.pamo.schedule.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a subject in Pjatk.
 *
 * @property interval The time interval during which the subject is held (e.g., "08:00-09:30").
 * @property room The room where the subject is conducted.
 * @property lecturers The lecturers teaching the subject.
 * @property codes The codes associated with the subject (could be course codes or similar identifiers).
 * @property names The names of the subject (could be the full name or description of the subject).
 * @property type The type of the subject (e.g., lecture, lab, seminar).
 * @property building The building where the subject is held.
 * @property groups The groups attending the subject.
 * @property studentsCount The count of students enrolled in the subject.
 */
@Serializable
data class PjatkSubject(
    val interval: String,
    val room: String,
    val lecturers: String,
    val codes: String,
    val names: String,
    val type: String,
    val building: String,
    val groups: String,
    val studentsCount: String
)
