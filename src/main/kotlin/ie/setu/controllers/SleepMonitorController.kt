package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.MeasurementDTO
import ie.setu.domain.SleepMonitorDTO
import ie.setu.domain.User
import ie.setu.domain.repository.SleepMonitorDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.HttpMethod
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.OpenApiParam
import io.javalin.plugin.openapi.annotations.OpenApiResponse

object SleepMonitorController {
    private val sleepMonitorDAO = SleepMonitorDAO()

    fun getAllSleepInfo(ctx: Context) {
        val sleepData = sleepMonitorDAO.getAll()
        if(sleepData.size!=0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(sleepData)
    }

    fun addSleepInfo(ctx: Context) {
        val sleepInfo : SleepMonitorDTO = jsonToObject(ctx.body())
        val addedSleepInfoID = sleepMonitorDAO.save(sleepInfo)
        if(addedSleepInfoID!=null){
            sleepInfo.id = addedSleepInfoID
            ctx.json(sleepInfo)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }

    }

    fun getSleepInfoByID(ctx: Context) {
        val sleepInfo = sleepMonitorDAO.findById(ctx.pathParam("sleepmonitor-id").toInt())
        if (sleepInfo != null) {
            ctx.json(sleepInfo)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Update Sleep info",
        operationId = "updateSleepInfoByID",
        tags = ["SleepMonitor"],
        path = "/api/sleepmonitoring/{sleepmonitor-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("sleepmonitor-id", Int::class, "The Sleep monitor ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateSleepInfoByID(ctx: Context) {
        val sleepInfo : SleepMonitorDTO = jsonToObject(ctx.body())
        if ((sleepMonitorDAO.updateByID(id = ctx.pathParam("sleepmonitor-id").toInt(), sleepInfo=sleepInfo)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteSleepInfoByID(ctx: Context) {
        if (sleepMonitorDAO.deleteByID(ctx.pathParam("sleepmonitor-id").toInt()) != 0)
            ctx.status(200)
        else
            ctx.status(404)
    }

    fun getSleepInfoByUserID(ctx: Context) {
        val sleepInfo = sleepMonitorDAO.findByUserId(ctx.pathParam("user-id").toInt())
        if (sleepInfo != null) {
            ctx.json(sleepInfo)
        }
    }

    fun updateSleepInfoByUserID(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val sleepInfo = mapper.readValue<SleepMonitorDTO>(ctx.body())
        sleepMonitorDAO.updateByUserID(
            userID = ctx.pathParam("user-id").toInt(),
            sleepInfo=sleepInfo)
    }

    fun deleteSleepInfoByUserID(ctx: Context) {
        if (sleepMonitorDAO.deleteByUserID(ctx.pathParam("user-id").toInt()) !=0 ){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }
}