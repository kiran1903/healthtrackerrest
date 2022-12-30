package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.helpers.*
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
class HealthParametersControllerTest {
    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadHealthParameters{
        @Test
        fun `get all health parameters from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/healthparameters/").asString()
            if (response.status == 200) {
                val retrievedHealthParameters: ArrayList<HealthParametersDC> = jsonToObject(response.body.toString())
                Assertions.assertNotEquals(0, retrievedHealthParameters.size)
            }
            else {
                Assertions.assertEquals(404, response.status)
            }
        }
        @Test
        fun `get health parameters by id when it does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/healthparameters/${id}").asString()

            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }
        @Test
        fun `get health parameters by user id when it does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val nonExistingUserID = 0
            val retrieveResponse = Unirest.get(origin + "/api/healthparameters/user/$nonExistingUserID").asString()
            // Assert -  verify return code
            Assertions.assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a health parameter entry by user id when user id exists, returns a 200 response`() {

            //Arrange - add the health parameter
            addHealthParameter(120.0, 110.2, 122.9, DateTime.now(), 4)

            //Assert - retrieve the added health parameter from the database and verify return code
            val retrieveResponse = retrieveHealthParametersByUserID(1)
            Assertions.assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added health parameter
            val retrievedHealthParameters : HealthParametersDC = jsonToObject(retrieveResponse.body.toString())
            deleteHealthParameterByID(retrievedHealthParameters.id)
        }
    }

    @Nested
    inner class DeleteHealthParameters{}
/*
    @Nested
    inner class UpdateHealthParameters{
        @Test
        fun `updating health parameter by id when it doesn't exist, returns a 404 response`() {
            val healthParamId = -1
            //Arrange - check there is no health parameter for -1 id
            Assertions.assertEquals(404, retrieveHealthParametersById(healthParamId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            Assertions.assertEquals(
                404, updateHealthParameterByID(
                    healthParamId, 123.6, 94.0,
                    120.0, DateTime.now(), 1
                ).status
            )
        }
        @Test
        fun `updating health parameter info by id when it exists, returns 204 response`() {

            //Arrange - add a health parameter entry that we plan to do an update on

            val addHealthParamResponse = addHealthParameter(
                healthparameters[0].bloodPressure,
                healthparameters[0].pulse, healthparameters[0].glucose,
                healthparameters[0].measuredOn, healthparameters[0].user_id)
            Assertions.assertEquals(201, addHealthParamResponse.status)
            val addedHealthParameterInfo = jsonNodeToObject<Activity>(addHealthParamResponse)

            //Act & Assert - update the added health parameter and assert a 204 is returned

            val updatedHealthParamResponse = updateHealthParameterByID(addedHealthParameterInfo.id, 212.2,
                95.0, 300.9, DateTime.now(), addedHealthParameterInfo.id)
            Assertions.assertEquals(204, updatedHealthParamResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedHealthParamResponse = retrieveHealthParametersById(addedHealthParameterInfo.id)
            val updatedHealthParamInfo = jsonNodeToObject<HealthParametersDC>(retrievedHealthParamResponse)
            Assertions.assertEquals(212.2, updatedHealthParamInfo.bloodPressure)
            Assertions.assertEquals(95.0, updatedHealthParamInfo.pulse, 0.1)
            Assertions.assertEquals(300.9, updatedHealthParamInfo.glucose)
            Assertions.assertEquals(DateTime.now(), updatedHealthParamInfo.measuredOn)

            //After - delete the health parameter
            deleteHealthParameterByID(addedHealthParameterInfo.id)
        }
    }
*/
    @Nested
    inner class CreateHealthParameters{}

    //helper function to add a test health parameter to the database
    private fun addHealthParameter (bloodPressure: Double, pulse: Double, glucose: Double, measuredOn: DateTime, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/healthparameters")
            .body("{\"bloodPressure\":\"$bloodPressure\", \"pulse\":\"$pulse\", \"glucose\":\"$glucose\", \"measuredOn\":\"$measuredOn\", \"user_id\":\"$user_id\"}")
            .asJson()
    }
    //helper function to retrieve health parameters by id
    private fun retrieveHealthParametersById(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/healthparameters/${id}").asJson()
    }
    private fun deleteHealthParameterByID (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/healthparameters/$id").asString()
    }
    private fun retrieveHealthParametersByUserID(id : Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/healthparameters/user/${id}").asString()
    }
    private fun updateHealthParameterByID(id: Int, bloodPressure: Double, pulse: Double, glucose: Double,
                                      measuredOn: DateTime, user_id: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/healthparameters/$id")
            .body("""
                {
                  "bloodPressure":"$bloodPressure",
                  "pulse":$pulse,
                  "glucose":$glucose,
                  "measuredOn":"$measuredOn",
                  "user_id":$user_id
                }
            """.trimIndent()).asJson()
    }
}