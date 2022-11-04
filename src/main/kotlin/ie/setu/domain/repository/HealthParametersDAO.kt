package ie.setu.domain.repository

import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.db.Users
import ie.setu.utils.mapToHealthParameter
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class HealthParametersDAO {
        fun getAll(): ArrayList<HealthParametersDC> {
                val healthParametersList: ArrayList<HealthParametersDC> = arrayListOf()
                transaction {
                        HealthParameters.selectAll().map {
                                healthParametersList.add(mapToHealthParameter(it))
                        }
                }
                return healthParametersList
        }

        fun save(healthParameters: HealthParametersDC){
                transaction {
                        HealthParameters.insert {
                                it[userid] = healthParameters.userid
                                it[bloodPressure] = healthParameters.bloodPressure
                                it[pulse] = healthParameters.pulse
                                it[glucose] = healthParameters.glucose
                                it[bmi] = healthParameters.bmi
                        }
                }
        }

}