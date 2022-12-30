package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ExerciseTracker: Table("excercisetracker") {
    val id = integer("id").primaryKey().autoIncrement()
    val started = datetime("started")
    val day = varchar("day",10)
    val exercise = varchar("exercise",100)
    val duration = integer("duration")
    val user_id = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}