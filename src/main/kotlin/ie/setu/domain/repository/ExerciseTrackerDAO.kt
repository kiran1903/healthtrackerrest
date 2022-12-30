package ie.setu.domain.repository

import ie.setu.domain.ExerciseTrackerDC
import ie.setu.domain.db.ExerciseTracker
import ie.setu.utils.mapToExcerciseTracker
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ExerciseTrackerDAO {
    fun getAll(): ArrayList<ExerciseTrackerDC> {
        val excerciseList: ArrayList<ExerciseTrackerDC> = arrayListOf()
        transaction {
            ExerciseTracker.selectAll().map {
                excerciseList.add(mapToExcerciseTracker(it))
            }
        }
        return excerciseList
    }

    fun save(excerciseData: ExerciseTrackerDC) {
        transaction {
            ExerciseTracker.insert {
                it[day] = excerciseData.day
                it[exercise] = excerciseData.exercise
                it[duration] = excerciseData.duration
                it[user_id] = excerciseData.user_id
            }
        }

    }

    fun findByDay(day: String): List<ExerciseTrackerDC> {
        return transaction {
            ExerciseTracker.select() {ExerciseTracker.day eq day}
                .map{ mapToExcerciseTracker(it) }
        }
    }

    fun findByExercise(exercise: String): List<ExerciseTrackerDC> {
        return transaction {
            ExerciseTracker.select(){ExerciseTracker.exercise eq exercise}
                .map{ mapToExcerciseTracker(it) }
        }

    }

    fun delete(exercise: String) {
        return transaction{
            ExerciseTracker.deleteWhere{
                ExerciseTracker.exercise eq exercise
            }
        }
    }

    fun update(exercise: String, exerciseData: ExerciseTrackerDC) {
        transaction {
            ExerciseTracker.update ({
                ExerciseTracker.exercise eq exercise}) {
                it[day] = exerciseData.day
                it[duration] = exerciseData.duration
                it[user_id] = exerciseData.user_id
            }
        }
    }

}