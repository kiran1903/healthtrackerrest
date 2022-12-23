package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.repository.user1
import ie.setu.repository.user2
import ie.setu.repository.user3
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), user_id = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), user_id = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), user_id = 2)
)


val exercisetrackers = arrayListOf<ExerciseTrackerDC>(
    ExerciseTrackerDC(1,"Monday","Arms",10),
    ExerciseTrackerDC(2,"Tuesday","Triceps",15),
    ExerciseTrackerDC(3,"Wednesday","Abs",20)
)

val healthparameters = arrayListOf<HealthParametersDC>(
    HealthParametersDC(1,110.0,67.0,87.0,DateTime.now(),1),
    HealthParametersDC(2,113.0,69.0,92.0, DateTime.now(), 2),
    HealthParametersDC(3,124.0,75.0,89.0, DateTime.now(), 3)
)

val measurements = arrayListOf<MeasurementDTO>(
    MeasurementDTO(1,67.0,65.0,15.0, 1),
    MeasurementDTO(2,75.0,71.0,13.0, 2),
    MeasurementDTO(3,82.0,75.0,18.0, 3)
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

