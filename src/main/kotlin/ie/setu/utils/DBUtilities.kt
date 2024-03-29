package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    user_id = it[Activities.user_id]
)

fun mapToHealthParameter(it: ResultRow) = HealthParametersDC(
    id = it[HealthParameters.id],
    bloodPressure = it[HealthParameters.bloodPressure],
    glucose = it[HealthParameters.glucose],
    pulse = it[HealthParameters.pulse],
    measuredOn = it[HealthParameters.measuredOn],
    user_id = it[HealthParameters.user_id]
)
fun mapToMeasurementDTO(it: ResultRow) = MeasurementDTO(
    id = it[Measurements.id],
    weight = it[Measurements.weight],
    height = it[Measurements.height],
    bmi = it[Measurements.bmi],
    user_id = it[Measurements.user_id]
)


fun mapToExcerciseTracker(it: ResultRow) = ExerciseTrackerDC(
    id = it[ExerciseTracker.id],
    started = it[ExerciseTracker.started],
    day = it[ExerciseTracker.day],
    exercise = it[ExerciseTracker.exercise],
    duration = it[ExerciseTracker.duration],
    user_id = it[ExerciseTracker.user_id]
)

fun mapToSleepMonitorDTO(it: ResultRow) = SleepMonitorDTO(
    id = it[SleepMonitor.id],
    date = it[SleepMonitor.date],
    day = it[SleepMonitor.day],
    sleepDuration = it[SleepMonitor.sleepDuration],
    user_id = it[SleepMonitor.user_id]
)