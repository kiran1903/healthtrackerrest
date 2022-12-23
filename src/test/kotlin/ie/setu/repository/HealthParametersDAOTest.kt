package ie.setu.repository

import ie.setu.domain.HealthParametersDC
import ie.setu.domain.db.ExerciseTracker
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.repository.ExerciseTrackerDAO
import ie.setu.domain.repository.HealthParametersDAO
import ie.setu.helpers.healthparameters
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


val healthparameter1 = healthparameters.get(0)
val healthparameter2 = healthparameters.get(1)
val healthparameter3 = healthparameters.get(2)

internal fun populateHealthParametersTable(): HealthParametersDAO {
    SchemaUtils.create(HealthParameters)
    populateUserTable()
    val healthparameterdao = HealthParametersDAO()
    healthparameterdao.save(healthparameter1)
    healthparameterdao.save(healthparameter2)
    healthparameterdao.save(healthparameter3)
    return healthparameterdao
}

class HealthParametersDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadHealthParameters{

        @Test
        fun `getting all parameters Tracked from a populated table returns all rows`(){
            transaction {
                val healthparametersdao = populateHealthParametersTable()
                assertEquals(3,healthparametersdao.getAll().size)
            }
        }

        @Test
        fun `get parameter tracked by id that doesn't exist, results in no parameters tracked returned`(){
            transaction {
                val healthparametersdao = populateHealthParametersTable()
                assertEquals(null,healthparametersdao.findById(4))
            }
        }

        @Test
        fun `get parameter tracked by id that exists, results of a correct parameter tracked returned`(){
            transaction {
                val healthparametersdao = populateHealthParametersTable()
                assertEquals(healthparameter3,healthparametersdao.findById(3))
            }
        }

        @Test
        fun `get all parameters tracked over empty table returns none`(){
            transaction {
                SchemaUtils.create(HealthParameters)
                val healthparametersdao = HealthParametersDAO()
                assertEquals(0,healthparametersdao.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteHealthParameters{

        @Test
        fun `deleting a non-existent Health Parameter in table results in no deletion`(){
            transaction {
                val healthparameterdao = populateHealthParametersTable()
                assertEquals(3,healthparameterdao.getAll().size)
                healthparameterdao.delete(4)
                assertEquals(3,healthparameterdao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing health parameter in table results in record being deleted`(){
            transaction {
                val healthparameterdao = populateHealthParametersTable()
                assertEquals(3,healthparameterdao.getAll().size)
                healthparameterdao.delete(2)
                assertEquals(2,healthparameterdao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateHealthParameters{

        @Test
        fun `updating existing health parameters in table results in successful update`(){
            transaction {
                val healthparameterdao = populateHealthParametersTable()
                val healthparam2updated = HealthParametersDC(2,115.0,70.0,93.0, DateTime.now(),2)
                healthparameterdao.update(2,healthparam2updated)
                assertEquals(115.0,healthparam2updated.bloodPressure)
            }
        }

        @Test
        fun `updating non-existent health parameters in table results in no updates`(){
            transaction {
                val healthparameterdao = populateHealthParametersTable()
                val healthparameter4updated = HealthParametersDC(4,115.0,70.0,93.0,DateTime.now(),3)
                healthparameterdao.update(4,healthparameter4updated)
                assertEquals(null,healthparameterdao.findById(4))
            }
        }

    }

}