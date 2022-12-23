package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object HealthParameters: Table("healthparameters") {
    val id = integer("id").autoIncrement().primaryKey()
    val bloodPressure = double("bloodpressure")
    val pulse = double("pulse")
    val glucose = double("glucose")
    val measuredOn = datetime("measuredOn")
    val user_id = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}