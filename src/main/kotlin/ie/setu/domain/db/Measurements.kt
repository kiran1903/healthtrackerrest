package ie.setu.domain.db

import ie.setu.domain.db.HealthParameters.autoIncrement
import ie.setu.domain.db.HealthParameters.primaryKey
import ie.setu.domain.db.HealthParameters.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Measurements : Table("measurements") {

    val id = integer("id").autoIncrement().primaryKey()
    val weight = double("weight")
    val height = double("height")
    val bmi = double("bmi")
    val user_id = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}