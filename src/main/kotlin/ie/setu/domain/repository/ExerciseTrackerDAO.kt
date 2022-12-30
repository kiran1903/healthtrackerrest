package ie.setu.domain.repository

import ie.setu.domain.ExerciseTrackerDC
import ie.setu.domain.db.Activities
import ie.setu.domain.db.ExerciseTracker
import ie.setu.domain.db.HealthParameters
import ie.setu.utils.mapToExcerciseTracker
import ie.setu.utils.mapToHealthParameter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    fun save(excerciseData: ExerciseTrackerDC): Int? {
        return transaction {
            ExerciseTracker.insert {
                it[day] = excerciseData.day
                it[started] = excerciseData.started
                it[exercise] = excerciseData.exercise
                it[duration] = excerciseData.duration
                it[user_id] = excerciseData.user_id
            }
        }get ExerciseTracker.id

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
                it[started] = exerciseData.started
                it[duration] = exerciseData.duration
                it[user_id] = exerciseData.user_id
            }
        }
    }
    fun updateByID(ID: Int, exerciseData: ExerciseTrackerDC):Int {
        return transaction {
            ExerciseTracker.update ({
                ExerciseTracker.id eq ID}) {
                it[day] = exerciseData.day
                it[exercise] = exerciseData.exercise
                it[started] = exerciseData.started
                it[duration] = exerciseData.duration
                it[user_id] = exerciseData.user_id
            }
        }
    }

    fun findById(ID: Int): ExerciseTrackerDC? {
        return transaction {
            ExerciseTracker.select() {
                ExerciseTracker.id eq ID}
                .map{ mapToExcerciseTracker(it) }
                .firstOrNull()
        }
    }

    fun deleteByID(ID: Int) : Int{
        return transaction{
            ExerciseTracker.deleteWhere { ExerciseTracker.id eq ID }
        }
    }

}