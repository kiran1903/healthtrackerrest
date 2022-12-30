package ie.setu.repository

import ie.setu.domain.ExerciseTrackerDC
import ie.setu.domain.db.ExerciseTracker
import ie.setu.domain.repository.ExerciseTrackerDAO
import ie.setu.helpers.exercisetrackers
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

val exercisetracker1 = exercisetrackers.get(0)
val exercisetracker2 = exercisetrackers.get(1)
val exercisetracker3 = exercisetrackers.get(2)

val emptylist = arrayListOf<ExerciseTrackerDC>()

internal fun populateExerciseTrackerTable(): ExerciseTrackerDAO {
    SchemaUtils.create(ExerciseTracker)
    val exercisetrackerdao = ExerciseTrackerDAO()
    exercisetrackerdao.save(exercisetracker1)
    exercisetrackerdao.save(exercisetracker2)
    exercisetrackerdao.save(exercisetracker3)
    return exercisetrackerdao
}

class ExerciseTrackerDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadExerciseTracker{

        @Test
        fun `getting all exercises Tracked from a populated table returns all rows`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()
                assertEquals(3,exercisetrackerdao.getAll().size)
            }
        }

        @Test
        fun `get exercise tracked by day that doesn't exist, results in no exercise tracked returned`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()
                assertContentEquals(emptylist,exercisetrackerdao.findByDay("Friday"))
            }
        }

        @Test
        fun `get exercise tracked by exercise that exists, results in a correct exercise tracked returned`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()
                assertEquals("Arms",exercisetrackerdao.findByExercise("Arms")[0].exercise)
            }
        }

        @Test
        fun `get all exercises tracked over empty table returns none`(){
            transaction {
                SchemaUtils.create(ExerciseTracker)
                val exercisetrackerdao = ExerciseTrackerDAO()
                assertEquals(0,exercisetrackerdao.getAll().size)
            }
        }
    }

    @Nested
    inner class CreateExerciseTracker{

        @Test
        fun `multiple exercises tracked added to table can be retrieved successfully`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()

                assertEquals(3,exercisetrackerdao.getAll().size)
                assertEquals(exercisetracker1.exercise,"Arms")
                assertEquals(exercisetracker2.exercise,"Triceps")
                assertEquals(exercisetracker3.exercise,"Abs")
            }
        }
    }

    @Nested
    inner class DeleteExercisesTracked{

        @Test
        fun `deleting a non-existent exercise tracked in table results in no deletion`(){
            transaction {
                populateUserTable()
                val exercisedao = populateExerciseTrackerTable()
                assertEquals(3,exercisedao.getAll().size)
                exercisedao.delete("Biceps")
                assertEquals(3,exercisedao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing exercise tracked in table results in record being deleted`(){
            transaction {
                populateUserTable()
                val exercisedao = populateExerciseTrackerTable()
                assertEquals(3,exercisedao.getAll().size)
                exercisedao.delete("Triceps")
                assertEquals(2,exercisedao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateExercisesTracked{

        @Test
        fun `updating existing exercise tracked in table results in successful update`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()
                val exercise2updated = ExerciseTrackerDC(2, DateTime.now(),"Tuesday","Biceps",15, 2)
                exercisetrackerdao.update("Biceps",exercise2updated)
                assertEquals(exercise2updated,exercise2updated)
            }
        }

        @Test
        fun `updating non-existent exercise tracked in table results in no updates`(){
            transaction {
                populateUserTable()
                val exercisetrackerdao = populateExerciseTrackerTable()
                val exercise4updated = ExerciseTrackerDC(4, DateTime.now(),"Thursday","Legs",25, 3)
                exercisetrackerdao.update("Legs",exercise4updated)
                assertEquals(3,exercisetrackerdao.getAll().size)
            }
        }

    }


}