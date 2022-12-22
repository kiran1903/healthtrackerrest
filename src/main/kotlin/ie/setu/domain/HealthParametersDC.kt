package ie.setu.domain

data class HealthParametersDC (
    val id: Int,
    val bloodPressure: Double,
    val pulse: Double,
    val glucose: Double,
    val user_id: Int
        )