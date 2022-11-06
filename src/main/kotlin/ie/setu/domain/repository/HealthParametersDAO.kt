package ie.setu.domain.repository

import ie.setu.domain.HealthParametersDC
import ie.setu.domain.User
import ie.setu.domain.db.HealthParameters
import ie.setu.domain.db.Users
import ie.setu.utils.mapToHealthParameter
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
                        }
                }
        }

    fun findById(userID: Int): HealthParametersDC? {
            return transaction {
                    HealthParameters.select() {
                            HealthParameters.userid eq userID}
                            .map{ mapToHealthParameter(it) }
                            .firstOrNull()
            }

    }

        fun delete(userID: Int) {
                return transaction{
                        HealthParameters.deleteWhere{
                                HealthParameters.userid eq userID
                        }
                }
        }

        fun update(userid: Int, healthParamerts: HealthParametersDC) {
                transaction {
                        HealthParameters.update ({
                                HealthParameters.userid eq userid}) {
                                it[pulse] = healthParamerts.pulse
                                it[bloodPressure] = healthParamerts.bloodPressure
                                it[glucose] = healthParamerts.glucose
                        }
                }
        }

}