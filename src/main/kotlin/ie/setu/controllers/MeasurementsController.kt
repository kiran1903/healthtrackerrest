package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.MeasurementDTO
import ie.setu.domain.repository.MeasurementDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object MeasurementsController {

    private val measurementDAO = MeasurementDAO()

    @OpenApi(
        summary = "Get all measurements",
        operationId = "getAllMeasurements",
        tags = ["Measurements"],
        path = "/api/measurements",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<MeasurementDTO>::class)])]
    )
    fun getAllMeasurements(ctx: Context) {
        val measurements = measurementDAO.getAll()
        if(measurements.size!=0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(measurements)
    }

    @OpenApi(
        summary = "Add measurements",
        operationId = "addMeasurements",
        tags = ["Measurements"],
        path = "/api/measurements",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addMeasurements(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val measurementData = mapper.readValue<MeasurementDTO>(ctx.body())
        val measurementID = measurementDAO.save(measurementData)
        if (measurementID != null) {
            measurementData.id = measurementID
            ctx.json(measurementData)
            ctx.status(200)
        }
        else
            ctx.status(404)

    }

    @OpenApi(
        summary = "Get measurements by user id",
        operationId = "getMeasurementsByUserId",
        tags = ["Measurements"],
        path = "/api/measurements/{userid}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("userid", Int::class, "The user id")],
        responses  = [OpenApiResponse("200", [OpenApiContent(MeasurementDTO::class)])]
    )
    fun getMeasurementsByUserId(ctx: Context) {
        val userData = measurementDAO.findByUserId(ctx.pathParam("userid").toInt())
        if (userData != null) {
            ctx.json(userData)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete measurements",
        operationId = "deleteMeasurements",
        tags = ["Measurements"],
        path = "/api/measurements/{userid}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("userid", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteMeasurements(ctx: Context) {
        if (measurementDAO.delete(ctx.pathParam("userid").toInt()) != 0)
            ctx.status(200)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update measurement",
        operationId = "updateMeasurements",
        tags = ["Measurements"],
        path = "/api/measurements/{userid}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("userid", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateMeasurements(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val measurementUpdates = mapper.readValue<MeasurementDTO>(ctx.body())
        if(measurementDAO.update(
            userid = ctx.pathParam("userid").toInt(),
            measurements=measurementUpdates) !=0)
            ctx.status(200)
        else
            ctx.status(404)
    }

}