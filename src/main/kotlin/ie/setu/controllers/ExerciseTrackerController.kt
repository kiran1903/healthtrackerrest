package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.ExerciseTrackerDC
import ie.setu.domain.repository.ExerciseTrackerDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*


object ExerciseTrackerController {
    private val exerciseTrackerDAO = ExerciseTrackerDAO()

    @OpenApi(
        summary = "Get exercise information",
        operationId = "getExerciseInfo",
        tags = ["Exercise"],
        path = "/api/exercisetracker",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<ExerciseTrackerDC>::class)])]
    )
    fun getExerciseInfo(ctx: Context) {
        val exercises = exerciseTrackerDAO.getAll()
        if(exercises.size!=0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(exercises)
    }

    @OpenApi(
        summary = "Add exercise info",
        operationId = "addExerciseInfo",
        tags = ["Exercise"],
        path = "/api/exercisetracker",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addExerciseInfo(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val exerciseData = mapper.readValue<ExerciseTrackerDC>(ctx.body())
        val id = exerciseTrackerDAO.save(exerciseData)
        if (id != null){
            exerciseData.id = id
            ctx.json(exerciseData)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }

    }

    @OpenApi(
        summary = "Get exercise info by day",
        operationId = "getExerciseInfoByDay",
        tags = ["Exercise"],
        path = "/api/exercisetracker/{day}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("day", Int::class, "Day of exercise")],
        responses  = [OpenApiResponse("200", [OpenApiContent(ExerciseTrackerDC::class)])]
    )
    fun getExerciseInfoByDay(ctx: Context) {
        val exerciseData = exerciseTrackerDAO.findByDay(ctx.pathParam("day"))
        if (exerciseData != null) {
            ctx.json(exerciseData)
        }
    }

    @OpenApi(
        summary = "Get exercise info by Exercise",
        operationId = "getExerciseInfoByExercise",
        tags = ["Exercise"],
        path = "/api/exercisetracker/{day}/{exercise}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("exercise", Int::class, "Exercise name")],
        responses  = [OpenApiResponse("200", [OpenApiContent(ExerciseTrackerDC::class)])]
    )
    fun getExerciseInfoByExercise(ctx: Context) {
        var exerciseData = exerciseTrackerDAO.findByExercise(ctx.pathParam("exercise"))
        if(exerciseData != null){
            ctx.json(exerciseData)
        }

    }

    @OpenApi(
        summary = "Delete Exercise info",
        operationId = "deleteExerciseInfo",
        tags = ["Exercise"],
        path = "/api/exercisetracker/{day}/{exercise}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("exercise", Int::class, "Exercise name")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteExerciseInfo(ctx: Context) {
        exerciseTrackerDAO.delete(ctx.pathParam("exercise"))
    }

    @OpenApi(
        summary = "Update exercise info",
        operationId = "updateExerciseInfo",
        tags = ["Exercise"],
        path = "/api/exercisetracker/{day}/{exercise}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("exercise", Int::class, "Exercise name")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateExerciseInfo(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val exerciseData = mapper.readValue<ExerciseTrackerDC>(ctx.body())
        exerciseTrackerDAO.update(
            exercise = ctx.pathParam("exercise"),
            exerciseData=exerciseData)
    }
    fun updateExerciseInfoByID(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

        val exerciseData = mapper.readValue<ExerciseTrackerDC>(ctx.body())
        val response = exerciseTrackerDAO.updateByID(
            ID = ctx.pathParam("exercise-id").toInt(),
            exerciseData=exerciseData)
        if(response != 0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun getExerciseInfoByID(ctx: Context) {
        val exerciseInfo = exerciseTrackerDAO.findById(ctx.pathParam("exercise-id").toInt())
        if (exerciseInfo != null) {
            ctx.json(exerciseInfo)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteExerciseInfoByID(ctx: Context) {
        if (exerciseTrackerDAO.deleteByID(ctx.pathParam("exercise-id").toInt()) !=0)
            ctx.status(200)
        else
            ctx.status(404)
    }

}