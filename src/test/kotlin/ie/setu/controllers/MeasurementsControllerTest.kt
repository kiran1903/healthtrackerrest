package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.MeasurementDTO
import ie.setu.helpers.ServerContainer
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MeasurementsControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadMeasurements{
        @Test
        fun `get all measurements from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/measurements/").asString()
            if (response.status == 200) {
                val retrievedMeasurements: ArrayList<MeasurementDTO> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedMeasurements.size)
            }
            else {
                Assertions.assertEquals(404, response.status)
            }
        }
        @Test
        fun `get measurements by id when it does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/measurements/${id}").asString()

            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }
    }
    @Nested
    inner class UpdateMeasurements{
        @Test
        fun `updating measurements by id when it doesn't exist, returns a 404 response`() {

            val ID = -1
            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            Assertions.assertEquals(
                404, updateMeasurements(
                    ID, 78.2, 140.2, 98.0, 2
                ).status
            )
        }
    }

    @Nested
    inner class CreateMeasurements{
        @Test
        fun `add measurements with correct details returns a 200 response`() {
            val addResponse = addMeasurements(76.3, 144.3, 78.7, 2)
            Assertions.assertEquals(200, addResponse.status)

            val addedMeasurements = jsonNodeToObject<MeasurementDTO>(addResponse)
            //delete
            Assertions.assertEquals(200, deleteMeasurementsById(addedMeasurements.id).status)
        }
    }

    private fun deleteMeasurementsById(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/measurements/$id").asString()
    }

    private fun addMeasurements(weight: Double, height: Double, bmi: Double, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/measurements")
            .body("""
                {
                  "weight":$weight,
                  "height":$height,
                  "bmi":$bmi,
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }

    @Nested
    inner class DeleteMeasurements{
        @Test
        fun `deleting measurements when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            Assertions.assertEquals(404, deleteMeasurementsById(-1).status)
        }
    }

    //helper functions
    private fun updateMeasurements(id: Int, weight: Double, height: Double, bmi: Double, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/measurements/$id")
            .body("""
                {
                  "weight":$weight,
                  "height":$height,
                  "bmi":$bmi,
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }

}