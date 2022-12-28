package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.HealthParametersDC
import ie.setu.domain.repository.HealthParametersDAO
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
        val parameters = healthParametersDao.findByUserId(ctx.pathParam("user-id").toInt())
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
    fun deleteParametersByID(ctx: Context) {
        healthParametersDao.deleteByID(ctx.pathParam("healthParamID").toInt())
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
    fun updateParametersByID(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthParameters = mapper.readValue<HealthParametersDC>(ctx.body())
        healthParametersDao.update(
            healthParamID = ctx.pathParam("healthParamID").toInt(),
            healthParamerts=healthParameters)
    }

    @OpenApi(
        summary = "Get parameters by id",
        operationId = "getParametersbyID",
        tags = ["Health Parameters"],
        path = "/api/healthparameters/{healthparameter-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("healthparameter-id", Int::class, "The healthparameters id")],
        responses  = [OpenApiResponse("200", [OpenApiContent(HealthParametersDC::class)])]
    )
    fun getParametersbyID(ctx: Context) {
        val healthParameters = healthParametersDao.findById(ctx.pathParam("healthparameter-id").toInt())
        if (healthParameters != null) {
            ctx.json(healthParameters)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }
    @OpenApi(
        summary = "Update health parameters by user id",
        operationId = "updateParametersByUserID",
        tags = ["Health Parameters"],
        path = "/api/healthparameters/user/{userid}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("userid", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateParametersByUserID(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val healthParameters = mapper.readValue<HealthParametersDC>(ctx.body())
        healthParametersDao.updateByUserID(
            userID = ctx.pathParam("user-id").toInt(),
            healthParamerts=healthParameters)
    }

    fun deleteParametersByUserID(ctx: Context) {
        healthParametersDao.deleteByUserID(ctx.pathParam("user-id").toInt())
    }

}