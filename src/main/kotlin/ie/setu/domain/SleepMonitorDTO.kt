package ie.setu.domain

import org.joda.time.DateTime

data class SleepMonitorDTO (
    var id:Int,
    var date: DateTime,
    var day:String,
    var sleepDuration:Double,
    var user_id:Int
        )