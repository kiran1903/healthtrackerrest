package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object ExerciseTracker: Table("excercisetracker") {
    val id = integer("id").primaryKey().autoIncrement()
    val day = varchar("day",10)
    val exercise = varchar("exercise",100)
    val duration = integer("duration")
}