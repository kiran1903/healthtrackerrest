package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.ExerciseTrackerDC
import ie.setu.domain.repository.ExerciseTrackerDAO
import io.javalin.http.Context

object ExerciseTrackerController {
    private val exerciseTrackerDAO = ExerciseTrackerDAO()
    
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

    fun addExerciseInfo(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val exerciseData = mapper.readValue<ExerciseTrackerDC>(ctx.body())
        exerciseTrackerDAO.save(exerciseData)
        ctx.json(exerciseData)
    }

    fun getExerciseInfoByDay(ctx: Context) {
        val exerciseData = exerciseTrackerDAO.findByDay(ctx.pathParam("day"))
        if (exerciseData != null) {
            ctx.json(exerciseData)
        }
    }

    fun getExerciseInfoByExercise(ctx: Context) {
        var exerciseData = exerciseTrackerDAO.findByExercise(ctx.pathParam("exercise"))
        if(exerciseData != null){
            ctx.json(exerciseData)
        }

    }

    fun deleteExerciseInfo(ctx: Context) {
        exerciseTrackerDAO.delete(ctx.pathParam("exercise"))
    }

    fun updateExerciseInfo(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val exerciseData = mapper.readValue<ExerciseTrackerDC>(ctx.body())
        exerciseTrackerDAO.update(
            exercise = ctx.pathParam("exercise"),
            exerciseData=exerciseData)
    }

}