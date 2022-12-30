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
import io.javalin.plugin.rendering.vue.VueComponent
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
        }.start(getRemoteAssignedPort())

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

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
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
                path("{healthparameter-id}"){
                    get(HealthParametersController::getParametersbyID)
                    delete(HealthParametersController::deleteParametersByID)
                    patch(HealthParametersController::updateParametersByID)
                }
                path("user/{user-id}"){
                    get(HealthParametersController::getParametersByUserId)
                    delete(HealthParametersController::deleteParametersByUserID)
                    patch(HealthParametersController::updateParametersByUserID)
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
                path("{exercise-id}"){
                    get(ExerciseTrackerController::getExerciseInfoByID)
                    patch(ExerciseTrackerController::updateExerciseInfoByID)
                    delete(ExerciseTrackerController::deleteExerciseInfoByID)
                }
                path("{day}"){
                    get(ExerciseTrackerController::getExerciseInfoByDay)
                    path("{exercise}"){
                        get(ExerciseTrackerController::getExerciseInfoByExercise)
                        delete(ExerciseTrackerController::deleteExerciseInfo)
                        patch(ExerciseTrackerController::updateExerciseInfo)
                    }
                }
            }
            //sleep monitoring
            path("/api/sleepmonitoring"){
                get(SleepMonitorController::getAllSleepInfo)
                post(SleepMonitorController::addSleepInfo)
                path("{sleepmonitor-id}"){
                    get(SleepMonitorController::getSleepInfoByID)
                    patch(SleepMonitorController::updateSleepInfoByID)
                    delete(SleepMonitorController::deleteSleepInfoByID)
                }
                path("/user/{user-id}"){
                    get(SleepMonitorController::getSleepInfoByUserID)
                    patch(SleepMonitorController::updateSleepInfoByUserID)
                    delete(SleepMonitorController::deleteSleepInfoByUserID)
                }
            }
            //stress monitoring
            //food tracker
            //water intake
            //goal setting

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

            //Activities
            get("/activities",VueComponent("<activities-overview></activities-overview>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))

            //Health parameters
            get("/healthparameters",VueComponent("<healthparameters-overview></healthparameters-overview>"))

            //Sleep monitor
            get("/sleepmonitor",VueComponent("<sleepmonitor-overview></sleepmonitor-overview>"))

            //Measurements
            get("/measurements",VueComponent("<measurements-overview></measurements-overview>"))

            //Exercise Tracker
            get("/exercise",VueComponent("<exercise-overview></exercise-overview>"))

        }
    }
}