package ie.setu.domain.repository


import ie.setu.domain.SleepMonitorDTO
import ie.setu.domain.db.Measurements
import ie.setu.domain.db.SleepMonitor
import ie.setu.utils.mapToSleepMonitorDTO
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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

    fun save(sleepInfo: SleepMonitorDTO) {
        transaction {
            SleepMonitor.insert {
                it[id] = sleepInfo.id
                it[date] = sleepInfo.date
                it[day] = sleepInfo.day
                it[sleepDuration] = sleepInfo.sleepDuration
                it[user_id] = sleepInfo.user_id
            }
        }

    }
}