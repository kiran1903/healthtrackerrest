package ie.setu.repository

import ie.setu.domain.MeasurementDTO
import ie.setu.domain.SleepMonitorDTO
import ie.setu.domain.db.Measurements
import ie.setu.domain.db.SleepMonitor
import ie.setu.domain.repository.MeasurementDAO
import ie.setu.domain.repository.SleepMonitorDAO
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.sleepMonitorInfo
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


val sleepInfo1 = sleepMonitorInfo[0]
val sleepInfo2 = sleepMonitorInfo[1]
val sleepInfo3 = sleepMonitorInfo[2]

class SleepMonitorDAOTest {
    companion object{
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateSleepInfo{
        @Test
        fun `Multiple sleep information added to table can be retrieved successfully`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()

                assertEquals(3, sleepInfoDAO.getAll().size)
                assertEquals(sleepInfo1,sleepInfoDAO.findByUserId(sleepInfo1.id))
                assertEquals(sleepInfo2,sleepInfoDAO.findByUserId(sleepInfo2.id))
                assertEquals(sleepInfo3,sleepInfoDAO.findByUserId(sleepInfo3.id))
            }
        }
    }
    @Nested
    inner class ReadSleepInfo{
        @Test
        fun `getting all sleep information from a populated table returns all rows`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                assertEquals(3,sleepInfoDAO.getAll().size)
            }
        }
        @Test
        fun `get sleep information by id that doesn't exist, results in no sleep information returned`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                assertEquals(null,sleepInfoDAO.findByUserId(4))
            }
        }
        @Test
        fun `get sleep information by id that exists, results of a correct sleep information returned`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                assertEquals(sleepInfo1,sleepInfoDAO.findByUserId(1))
            }
        }
        @Test
        fun `get all sleep information over empty table returns none`(){
            transaction {
                SchemaUtils.create(SleepMonitor)
                val sleepInfoDAO = SleepMonitorDAO()
                assertEquals(0,sleepInfoDAO.getAll().size)
            }
        }
    }
    @Nested
    inner class UpdateSleepInfo{
        @Test
        fun `updating existing sleep information in table results in successful update`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                val sleepInfo2updated = SleepMonitorDTO(2, DateTime.now(),"Tuesday",6.5,2)
                sleepInfoDAO.updateByID(2,sleepInfo2updated)
                sleepInfoDAO.findByUserId(2)?.let { assertEquals(6.5, it.sleepDuration) }
            }
        }
        @Test
        fun `updating non-existent sleep information in table results in no updates`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                val sleepInfo4updated = SleepMonitorDTO(4, DateTime.now(),"Sunday",9.0, 4)
                sleepInfoDAO.updateByID(4,sleepInfo4updated)
                assertEquals(null,sleepInfoDAO.findByUserId(4))
            }
        }
    }
    @Nested
    inner class DeleteSleepInfo{
        @Test
        fun `deleting a non-existent sleep information in table results in no deletion`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                assertEquals(3,sleepInfoDAO.getAll().size)
                sleepInfoDAO.deleteByID(4)
                assertEquals(3,sleepInfoDAO.getAll().size)
            }
        }
        @Test
        fun `deleting an existing sleep information in table results in record being deleted`(){
            transaction {
                populateUserTable()
                val sleepInfoDAO = populateSleepMonitorTable()
                assertEquals(3,sleepInfoDAO.getAll().size)
                sleepInfoDAO.deleteByID(2)
                assertEquals(2,sleepInfoDAO.getAll().size)
            }
        }
    }

}
internal fun populateSleepMonitorTable(): SleepMonitorDAO {
    SchemaUtils.create(SleepMonitor)
    val sleepMonitorDAO = SleepMonitorDAO()
    sleepMonitorDAO.save(sleepInfo1)
    sleepMonitorDAO.save(sleepInfo2)
    sleepMonitorDAO.save(sleepInfo3)
    return sleepMonitorDAO
}