package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.MeasurementDTO
import ie.setu.domain.User
import ie.setu.domain.repository.MeasurementDAO
import io.javalin.http.Context

object MeasurementsController {
    //private val userDao = UserDAO()
    private val measurementDAO = MeasurementDAO()

    //--------------------------------------------------------------
    // Measurement Specifics
    //-------------------------------------------------------------

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

    fun addMeasurements(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val measurementData = mapper.readValue<MeasurementDTO>(ctx.body())
        measurementDAO.save(measurementData)
        ctx.json(measurementData)
    }

    fun getMeasurementsByUserId(ctx: Context) {
        val userData = measurementDAO.findByUserId(ctx.pathParam("userid").toInt())
        if (userData != null) {
            ctx.json(userData)
        }
    }

    fun deleteMeasurements(ctx: Context) {
        measurementDAO.delete(ctx.pathParam("userid").toInt())
    }

    fun updateMeasurements(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val measurementUpdates = mapper.readValue<MeasurementDTO>(ctx.body())
        measurementDAO.update(
            userid = ctx.pathParam("userid").toInt(),
            measurements=measurementUpdates)
    }

}