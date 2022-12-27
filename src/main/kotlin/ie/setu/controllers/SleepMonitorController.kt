package ie.setu.controllers

import ie.setu.domain.SleepMonitorDTO
import ie.setu.domain.User
import ie.setu.domain.repository.SleepMonitorDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

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
        sleepMonitorDAO.save(sleepInfo)
        ctx.json(sleepInfo)
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

    fun updateSleepInfoByID(ctx: Context) {
        val sleepInfo : SleepMonitorDTO = jsonToObject(ctx.body())
        if ((sleepMonitorDAO.updateByID(id = ctx.pathParam("sleepmonitor-id").toInt(), sleepInfo=sleepInfo)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}