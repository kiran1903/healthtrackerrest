package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info

class JavalinConfig {
    //val caloriesTrackerController : ICaloriesTrackerController = CaloriesTrackerController

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)
                path("{user-id}"){
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)
                    //path("activities"){
                        //get(HealthTrackerController::getActivitiesByUserId)
                        //delete(HealthTrackerController::deleteActivityByUserId)
                    //}
                }
                path("/email/{email}"){
                    get(HealthTrackerController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(HealthTrackerController::getAllActivities)
                post(HealthTrackerController::addActivity)
                path("{activity-id}") {
                    get(HealthTrackerController::getActivitiesByActivityId)
                    delete(HealthTrackerController::deleteActivityByActivityId)
                    patch(HealthTrackerController::updateActivity)
                }
                path("/user/{user-id}") {
                    get(HealthTrackerController::getActivitiesByUserId)
                    delete(HealthTrackerController::deleteActivityByUserId)
                }
            }
            path("/api/healthparameters"){
                get(HealthParametersController::getAllParameters)
                post(HealthParametersController::addEntry)
                path("{userid}"){
                    get(HealthParametersController::getParametersByUserId)
                    delete(HealthParametersController::deleteParameters)
                    patch(HealthParametersController::updateParameters)
                }
            }
            path("/api/measurements"){
                get(MeasurementsController::getAllMeasurements)
                post(MeasurementsController::addMeasurements)
                path("{userid}"){
                    get(MeasurementsController::getMeasurementsByUserId)
                    delete(MeasurementsController::deleteMeasurements)
                    patch(MeasurementsController::updateMeasurements)
                }
            }

            //Excercise tracker
            path("/api/exercisetracker"){
                get(ExerciseTrackerController::getExerciseInfo)
                post(ExerciseTrackerController::addExerciseInfo)
                path("{day}"){
                    get(ExerciseTrackerController::getExerciseInfoByDay)
                    path("{exercise}"){
                        get(ExerciseTrackerController::getExerciseInfoByExercise)
                        delete(ExerciseTrackerController::deleteExerciseInfo)
                        patch(ExerciseTrackerController::updateExerciseInfo)
                    }
                }
            }
        }
    }
}