package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.domain.repository.HealthParametersDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object HealthParametersController {

    private val healthParametersDao = HealthParametersDAO()

    fun getAllParameters(ctx: Context) {
        ctx.json(healthParametersDao.getAll())
    }

    fun addEntry(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthParameters = mapper.readValue<HealthParametersDC>(ctx.body())
        healthParametersDao.save(healthParameters)
        ctx.json(healthParameters)
    }
}