package ie.setu.domain.db


import ie.setu.domain.db.Measurements.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object SleepMonitor: Table("sleepmonitor") {
    val id = integer("id").autoIncrement().primaryKey()
    val date = datetime("date")
    val day = varchar("day",10)
    val sleepDuration = double("sleepduration")
    val user_id = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}