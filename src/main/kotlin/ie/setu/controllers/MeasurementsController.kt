package ie.setu.controllers

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

}