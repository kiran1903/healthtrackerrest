package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.ExerciseTrackerDC
import ie.setu.helpers.ServerContainer
import ie.setu.helpers.validEmail
import ie.setu.helpers.validName
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonObjectMapper
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.StringJoiner

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExerciseTrackerControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadExerciseInfo{

        @Test
        fun `get all exercise info from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/exercisetracker/").asString()
            if (response.status == 200) {
                val retrievedExerciseInfo: ArrayList<ExerciseTrackerDC> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedExerciseInfo.size)
            }
            else {
                Assertions.assertEquals(404, response.status)
            }
        }
        @Test
        fun `get exercise info by id when it does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/exercisetracker/${id}").asString()

            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }
    }

    @Nested
    inner class UpdateExerciseInfo{
        @Test
        fun `updating an exercise info by id when it doesn't exist, returns a 404 response`() {

            val exerciseID = -1


            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            Assertions.assertEquals(
                404, updateExercise(
                    exerciseID, "Sunday", DateTime.now(), "Legs", 25, 2
                ).status
            )
        }
    }

    @Nested
    inner class DeleteExerciseInfo{
        @Test
        fun `deleting an exercise info when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteExerciseInfoById(-1).status)
        }
    }


    @Nested
    inner class CreateExerciseInfo{
        @Test
        fun `add exercise info with correct details returns a 200 response`() {
            val addResponse = addExerciseInfo("Sunday", DateTime.now(), "Cardio", 20, 2)
            Assertions.assertEquals(201, addResponse.status)

            val addedExercise = jsonNodeToObject<ExerciseTrackerDC>(addResponse)
            //delete
            Assertions.assertEquals(200, deleteExerciseInfoById(addedExercise.id).status)
        }
    }

    private fun addExerciseInfo(day: String, started: DateTime?, exercise: String, duration: Int, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/exercisetracker")
            .body("{\"day\":\"$day\", \"started\":\"$started\", \"exercise\":\"$exercise\", \"duration\":\"$duration\", \"user_id\":\"$user_id\"}")
            .asJson()
    }

    private fun updateExercise(exerciseID: Int, day: String, started: DateTime?, exercise: String, duration: Int, user_id: Int): HttpResponse<JsonNode> {
        jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)


        return Unirest.patch(origin + "/api/exercisetracker/$exerciseID")
            .body("""
                {
                  "day":"$day",
                  "started":"$started",
                  "exercise":"$exercise",
                  "duration":$duration,
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }
    private fun deleteExerciseInfoById(Id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/exercisetracker/$Id").asString()
    }



}