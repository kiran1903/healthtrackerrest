package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object Measurements : Table("measurements") {

    val userid = integer("userid").autoIncrement().primaryKey()
    val weight = double("weight")
    val height = double("height")
    val bmi = double("bmi")
}