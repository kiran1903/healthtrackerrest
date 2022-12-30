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
val validName = "Test User 3"
val validEmail = "testuser3@test.com"

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
    ExerciseTrackerDC(id=1, started = DateTime.now(), day = "Monday", exercise = "Arms", duration = 10, user_id = 1),
    ExerciseTrackerDC(id=2, started = DateTime.now(), day = "Tuesday", exercise = "Triceps", duration = 15, user_id = 2),
    ExerciseTrackerDC(id=3, started = DateTime.now(), day = "Wednesday", exercise = "Abs", duration = 20, user_id = 3)
)

val healthparameters = arrayListOf<HealthParametersDC>(
    HealthParametersDC(1,110.0,67.0,87.0,DateTime.now(),1),
    HealthParametersDC(2,113.0,69.0,92.0, DateTime.now(), 2),
    HealthParametersDC(3,124.0,75.0,89.0, DateTime.now(), 3)
)

val measurements = arrayListOf<MeasurementDTO>(
    MeasurementDTO(1,67.0,65.0,15.857988165680473, 1),
    MeasurementDTO(2,75.0,71.0,14.878000396746678, 2),
    MeasurementDTO(3,82.0,75.0,14.577777777777778, 3)
)

val sleepMonitorInfo = arrayListOf<SleepMonitorDTO>(
    SleepMonitorDTO(1,DateTime.now(),"Monday",7.0,1),
    SleepMonitorDTO(2,DateTime.now(),"Tuesday",8.0, 2),
    SleepMonitorDTO(3,DateTime.now(),"Wednesday",8.5, 3)
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

