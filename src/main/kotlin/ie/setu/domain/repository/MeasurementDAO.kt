package ie.setu.domain.repository

import ie.setu.config.DbConfig
import ie.setu.domain.MeasurementDTO
import ie.setu.domain.db.Measurements
import ie.setu.utils.mapToMeasurementDTO
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MeasurementDAO {
    private val logger = KotlinLogging.logger {}
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

}