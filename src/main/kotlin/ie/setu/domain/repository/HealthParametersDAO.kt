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

        fun save(healthParameters: HealthParametersDC): Int?{
                return transaction {
                        HealthParameters.insert {

                                it[bloodPressure] = healthParameters.bloodPressure
                                it[pulse] = healthParameters.pulse
                                it[glucose] = healthParameters.glucose
                                it[measuredOn] = healthParameters.measuredOn
                                it[user_id] = healthParameters.user_id
                        }
                } get HealthParameters.id
        }

    fun findById(healthParamID: Int): HealthParametersDC? {
            return transaction {
                    HealthParameters.select() {
                            HealthParameters.id eq healthParamID}
                            .map{ mapToHealthParameter(it) }
                            .firstOrNull()
            }

    }

        fun deleteByID(healthParamID: Int) {
                return transaction{
                        HealthParameters.deleteWhere{
                                HealthParameters.id eq healthParamID
                        }
                }
        }

        fun update(healthParamID: Int, healthParamerts: HealthParametersDC): Int? {
                return transaction {
                        HealthParameters.update ({
                                HealthParameters.id eq healthParamID}) {
                                it[pulse] = healthParamerts.pulse
                                it[bloodPressure] = healthParamerts.bloodPressure
                                it[glucose] = healthParamerts.glucose
                                it[measuredOn] = healthParamerts.measuredOn
                                it[user_id] = healthParamerts.user_id
                        }
                }
        }
        fun updateByUserID(userID: Int, healthParamerts: HealthParametersDC) {
                transaction {
                        HealthParameters.update ({
                                HealthParameters.user_id eq userID}) {
                                it[pulse] = healthParamerts.pulse
                                it[bloodPressure] = healthParamerts.bloodPressure
                                it[glucose] = healthParamerts.glucose
                                it[measuredOn] = healthParamerts.measuredOn
                                it[user_id] = healthParamerts.user_id
                        }
                }
        }
        fun findByUserId(userID: Int): HealthParametersDC? {
                return transaction {
                        HealthParameters.select() {
                                HealthParameters.user_id eq userID}
                                .map{ mapToHealthParameter(it) }
                                .firstOrNull()
                }
        }

        fun deleteByUserID(userID: Int) {
                return transaction{
                        HealthParameters.deleteWhere{
                                HealthParameters.user_id eq userID
                        }
                }
        }


}