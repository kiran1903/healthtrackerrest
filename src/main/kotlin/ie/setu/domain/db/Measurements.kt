package ie.setu.domain.db

import ie.setu.domain.db.Users.autoIncrement
import ie.setu.domain.db.Users.primaryKey
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Measurements : Table("measurements") {
//    val id = integer("id").autoIncrement().primaryKey()
//    val weight = integer("weight")
//    val userid = integer("userid").references(Users.id, onDelete = ReferenceOption.CASCADE)

    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
}