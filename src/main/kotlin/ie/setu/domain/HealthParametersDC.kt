package ie.setu.domain

import org.joda.time.DateTime

data class HealthParametersDC (
    var id: Int,
    val bloodPressure: Double,
    val pulse: Double,
    val glucose: Double,
    val measuredOn: DateTime,
    val user_id: Int

        )