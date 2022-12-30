package ie.setu.domain.repository


import ie.setu.domain.SleepMonitorDTO
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.db.Measurements
import ie.setu.domain.db.SleepMonitor
import ie.setu.utils.mapToHealthParameter
import ie.setu.utils.mapToSleepMonitorDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SleepMonitorDAO {

    fun getAll(): ArrayList<SleepMonitorDTO> {
        val measurementsList: ArrayList<SleepMonitorDTO> = arrayListOf()

        transaction() {

            SleepMonitor.selectAll().map {
                measurementsList.add(mapToSleepMonitorDTO(it)) }
        }

        return measurementsList
    }

    fun save(sleepInfo: SleepMonitorDTO) :Int?{
        return transaction {
            SleepMonitor.insert {
                it[date] = sleepInfo.date
                it[day] = sleepInfo.day
                it[sleepDuration] = sleepInfo.sleepDuration
                it[user_id] = sleepInfo.user_id
            }
        }get SleepMonitor.id

    }

    fun findById(sleepMonitorID: Int): SleepMonitorDTO? {
        return transaction {
            SleepMonitor.select() {
                SleepMonitor.id eq sleepMonitorID}
                .map{ mapToSleepMonitorDTO(it) }
                .firstOrNull()
        }
    }

    fun updateByID(id: Int, sleepInfo: SleepMonitorDTO): Any {
        return transaction {
            SleepMonitor.update ({
                SleepMonitor.id eq id}) {
                it[date] = sleepInfo.date
                it[day] = sleepInfo.day
                it[sleepDuration] = sleepInfo.sleepDuration
                it[user_id] = sleepInfo.user_id
            }
        }
    }

    fun deleteByID(id: Int) : Int{
        return transaction{
            SleepMonitor.deleteWhere{
                SleepMonitor.id eq id
            }
        }
    }

    fun findByUserId(userID: Int): SleepMonitorDTO? {
        return transaction {
            SleepMonitor.select() {
                SleepMonitor.user_id eq userID}
                .map{ mapToSleepMonitorDTO(it) }
                .firstOrNull()
        }
    }

    fun updateByUserID(userID: Int, sleepInfo: SleepMonitorDTO) {
        transaction {
            SleepMonitor.update ({
                SleepMonitor.user_id eq userID}) {
                it[date] = sleepInfo.date
                it[day] = sleepInfo.day
                it[sleepDuration] = sleepInfo.sleepDuration
                it[user_id] = sleepInfo.user_id
            }
        }
    }

    fun deleteByUserID(userID: Int) :Int{
        return transaction{
            SleepMonitor.deleteWhere{
                SleepMonitor.user_id eq userID
            }
        }
    }
}