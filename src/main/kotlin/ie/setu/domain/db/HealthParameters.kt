package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object HealthParameters: Table("healthparameters") {
    val userid = integer("userid").primaryKey()
    val bloodPressure = double("bloodpressure")
    val pulse = double("pulse")
    val glucose = double("glucose")
    val bmi = double("bmi")
}