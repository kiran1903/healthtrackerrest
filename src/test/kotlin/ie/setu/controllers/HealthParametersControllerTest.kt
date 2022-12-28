package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
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
        fun `getting health parameters by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addHealthParameter(120.0, 110.2, 122.9, DateTime.now(), 4)
            val addedHealthParametersEntry : User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveHealthParametersById(addedHealthParametersEntry.id)
            Assertions.assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteHealthParameterByID(addedHealthParametersEntry.id)
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

    @Nested
    inner class UpdateHealthParameters{}

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
}