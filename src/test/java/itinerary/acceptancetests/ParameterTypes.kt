package itinerary.acceptancetests

import io.cucumber.java.ParameterType
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ParameterTypes {

    @ParameterType(".*")
    fun departureTimes(timesValue: String): List<LocalTime> {
        return timesValue
            .split(",")
            .map { it.trim() }
            .map { localTimeOf(it) }
    }

    private fun localTimeOf(timeValue: String): LocalTime =
        LocalTime.parse(timeValue, DateTimeFormatter.ofPattern("H:mm"))

    @ParameterType(".*")
    fun line(lineName: String): String = lineName

    @ParameterType(".*")
    fun station(stationName: String): String = stationName

    @ParameterType(".*")
    fun departureTime(departureTime: String): LocalTime = localTimeOf(departureTime)
}
