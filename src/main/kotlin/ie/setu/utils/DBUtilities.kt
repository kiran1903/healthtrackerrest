package ie.setu.utils

import ie.setu.domain.HealthParametersDC
import ie.setu.domain.MeasurementDTO
import ie.setu.domain.User
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.db.Measurements
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToHealthParameter(it: ResultRow) = HealthParametersDC(
    userid = it[HealthParameters.userid],
    bloodPressure = it[HealthParameters.bloodPressure],
    glucose = it[HealthParameters.glucose],
    bmi = it[HealthParameters.bmi],
    pulse = it[HealthParameters.pulse]
)
fun mapToMeasurementDTO(it: ResultRow) = MeasurementDTO(
    id = it[Measurements.id],
    //weight = it[Measurements.weight],
    //userid = it[Measurements.userid]
    name = it[Measurements.name],
    email = it[Measurements.email]
)