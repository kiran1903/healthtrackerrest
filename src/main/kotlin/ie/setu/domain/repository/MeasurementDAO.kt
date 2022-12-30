package ie.setu.domain.repository

import ie.setu.domain.MeasurementDTO
import ie.setu.domain.db.Measurements
import ie.setu.utils.mapToMeasurementDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MeasurementDAO {
    //Get all the Measurement items in the database regardless of user id
    fun getAll(): ArrayList<MeasurementDTO> {
        val measurementsList: ArrayList<MeasurementDTO> = arrayListOf()
        //val dbconf = DbConfig()
        //dbconf.getDbConnection()

        transaction() {

            Measurements.selectAll().map {
                measurementsList.add(mapToMeasurementDTO(it)) }
        }

        return measurementsList
    }

    fun save(measurementData: MeasurementDTO) :Int?{
        return transaction {
            Measurements.insert {
                it[height] = measurementData.height
                it[weight] = measurementData.weight
                //it[bmi] = measurementData.bmi
                it[bmi] = (measurementData.weight/(measurementData.height * measurementData.height)) * 1000
                it[user_id] = measurementData.user_id
            }
        } get Measurements.id
    }

    fun findByUserId(userId: Int): MeasurementDTO? {
        return transaction {
            Measurements.select() {
                Measurements.id eq userId}
                .map{ mapToMeasurementDTO(it) }
                .firstOrNull()
        }
    }

    fun delete(userID: Int) :Int?{
        return transaction{
            Measurements.deleteWhere{
                Measurements.id eq userID
            }
        }
    }

    fun update(userid: Int, measurements: MeasurementDTO): Int? {
        return transaction {
            Measurements.update ({
                Measurements.id eq userid}) {
                it[height] = measurements.height
                it[weight] = measurements.weight
                it[bmi] = measurements.bmi
                it[user_id] = measurements.user_id
            }
        }
    }

}