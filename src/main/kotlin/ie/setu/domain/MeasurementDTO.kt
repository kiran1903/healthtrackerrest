package ie.setu.domain

data class MeasurementDTO (var id: Int,
                           var weight:Double,
                           var height:Double,
                           var bmi:Double,
                           var user_id:Int)