package ie.setu.domain.repository

import ie.setu.domain.HealthParametersDC
import ie.setu.domain.db.HealthParameters
import ie.setu.utils.mapToHealthParameter
import org.jetbrains.exposed.sql.*
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
                                it[id] = healthParameters.id
                                it[bloodPressure] = healthParameters.bloodPressure
                                it[pulse] = healthParameters.pulse
                                it[glucose] = healthParameters.glucose
                                it[user_id] = healthParameters.user_id
                        }
                }
        }

    fun findById(userID: Int): HealthParametersDC? {
            return transaction {
                    HealthParameters.select() {
                            HealthParameters.id eq userID}
                            .map{ mapToHealthParameter(it) }
                            .firstOrNull()
            }

    }

        fun delete(userID: Int) {
                return transaction{
                        HealthParameters.deleteWhere{
                                HealthParameters.id eq userID
                        }
                }
        }

        fun update(userid: Int, healthParamerts: HealthParametersDC) {
                transaction {
                        HealthParameters.update ({
                                HealthParameters.id eq userid}) {
                                it[pulse] = healthParamerts.pulse
                                it[bloodPressure] = healthParamerts.bloodPressure
                                it[glucose] = healthParamerts.glucose
                                it[user_id] = healthParamerts.user_id
                        }
                }
        }

}