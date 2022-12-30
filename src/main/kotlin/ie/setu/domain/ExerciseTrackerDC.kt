package ie.setu.domain

import org.joda.time.DateTime

data class ExerciseTrackerDC (
    var id: Int,
    val started: DateTime,
    val day: String,
    val exercise: String,
    val duration: Int,
    val user_id: Int
    )