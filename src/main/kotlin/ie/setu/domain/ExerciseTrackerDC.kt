package ie.setu.domain

data class ExerciseTrackerDC (
    val id: Int,
    val day: String,
    val exercise: String,
    val duration: Int,
    val user_id: Int
    )