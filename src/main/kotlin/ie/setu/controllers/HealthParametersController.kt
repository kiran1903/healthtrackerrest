package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.repository.HealthParametersDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.OpenApi
import io.javalin.plugin.openapi.annotations.*

object HealthParametersController {

    private val healthParametersDao = HealthParametersDAO()

    @OpenApi(
        summary = "Get all health parameters",
        operationId = "getAllParameters",
        tags = ["Health Parameters"],
        path = "/api/healthparameters",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<HealthParametersDC>::class)])]
    )
    fun getAllParameters(ctx: Context) {
        ctx.json(healthParametersDao.getAll())
    }

    @OpenApi(
        summary = "Add Health parameter entry",
        operationId = "addEntry",
        tags = ["Health Parameters"],
        path = "/api/healthparameters",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addEntry(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthParameters = mapper.readValue<HealthParametersDC>(ctx.body())
        healthParametersDao.save(healthParameters)
        ctx.json(healthParameters)
    }

    @OpenApi(
        summary = "Get parameters by user id",
        operationId = "getParametersByUserId",
        tags = ["Health Parameters"],
        path = "/api/healthparameters/{userid}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("userid", Int::class, "The user id")],
        responses  = [OpenApiResponse("200", [OpenApiContent(HealthParametersDC::class)])]
    )
    fun getParametersByUserId(ctx: Context) {
        val parameters = healthParametersDao.findById(ctx.pathParam("userid").toInt())
        if (parameters != null) {
            ctx.json(parameters)
        }
    }

    @OpenApi(
        summary = "Delete parameters",
        operationId = "deleteParameters",
        tags = ["Health Parameters"],
        path = "/api/healthparameters/{userid}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("userid", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteParameters(ctx: Context) {
        healthParametersDao.delete(ctx.pathParam("userid").toInt())
    }

    @OpenApi(
        summary = "Update health parameters",
        operationId = "updateParameters",
        tags = ["Health Parameters"],
        path = "/api/healthparameters/{userid}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("userid", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateParameters(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthParameters = mapper.readValue<HealthParametersDC>(ctx.body())
        healthParametersDao.update(
            userid = ctx.pathParam("userid").toInt(),
            healthParamerts=healthParameters)
    }
}