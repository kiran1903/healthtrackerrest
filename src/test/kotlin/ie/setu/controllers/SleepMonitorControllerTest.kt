package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.SleepMonitorDTO
import ie.setu.helpers.ServerContainer
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepMonitorControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadSleepInfo{
        @Test
        fun `get all sleep info from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/sleepmonitoring/").asString()
            if (response.status == 200) {
                val retrievedSleepInfo: ArrayList<SleepMonitorDTO> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedSleepInfo.size)
            }
            else {
                Assertions.assertEquals(404, response.status)
            }
        }
        @Test
        fun `get sleep info by id when it does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/sleepmonitoring/${id}").asString()

            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }
    }
    @Nested
    inner class UpdateSleepInfo{
        @Test
        fun `updating an sleep info by id when it doesn't exist, returns a 404 response`() {

            val sleepID = -1

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            Assertions.assertEquals(
                404, updateSleepInfo(
                    sleepID, "Sunday", DateTime.now(),  8.0, 2
                ).status
            )
        }
    }
    @Nested
    inner class CreateSleepInfo{
        @Test
        fun `add sleep info with correct details returns a 200 response`() {
            val addResponse = addSleepInfo("Sunday", DateTime.now(), 8.5, 2)
            Assertions.assertEquals(200, addResponse.status)

            val addedSleepInfo = jsonNodeToObject<SleepMonitorDTO>(addResponse)
            //delete
            Assertions.assertEquals(200, deleteSleepInfoById(addedSleepInfo.id).status)
        }
    }



    @Nested
    inner class DeleteSleepInfo{
        @Test
        fun `deleting sleep info when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteSleepInfoById(-1).status)
        }
    }
    //helper functions
    private fun deleteSleepInfoById(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/sleepmonitoring/$id").asString()
    }


    private fun addSleepInfo(day: String, date: DateTime?, sleepDuration: Double, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/sleepmonitoring")
            .body("""
                {
                  "day":"$day",
                  "date":"$date",
                  "sleepDuration":$sleepDuration,
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }
    private fun updateSleepInfo(ID: Int, day: String, date: DateTime?, duration: Double, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/sleepmonitoring/$ID")
            .body("""
                {
                  "day":"$day",
                  "date":"$date",
                  "sleepDuration":$duration,
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }


}