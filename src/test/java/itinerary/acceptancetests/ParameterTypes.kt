package itinerary.acceptancetests

import io.cucumber.java.ParameterType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ParameterTypes {
    @ParameterType(".*")
    fun times(timeValue: String): List<LocalTime>{
        return timeValue
            .split(",")
            .map{it.trim()}
            .map{time(it)}
    }

    @ParameterType(".*")
    private fun time(timeValue: String): LocalTime =
        LocalTime.parse(timeValue, DateTimeFormatter.ofPattern("H:mm"))

}