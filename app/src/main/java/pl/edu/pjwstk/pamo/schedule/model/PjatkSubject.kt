package pl.edu.pjwstk.pamo.schedule.model

import kotlinx.serialization.Serializable

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
