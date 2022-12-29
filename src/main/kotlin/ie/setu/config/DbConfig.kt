package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
//heroku details
//            "jdbc:postgresql://ec2-52-207-15-147.compute-1.amazonaws.com:5432/d8audr61018bo9?sslmode=require",
//            driver = "org.postgresql.Driver",
//            user = "cpyakngkskdiyl",
//            password = "4fabe94fc943246ca8fda00f3c9638051069478f3d180eb20e3ecd2adca1e099")

            //elephantSQL
            "jdbc:postgresql://lucky.db.elephantsql.com:5432/scnscgcq",
            driver = "org.postgresql.Driver",
            user = "scnscgcq",
            password = "FL3bKEmwMYCb0x2F79GH90ZeL5aHA5mU")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}