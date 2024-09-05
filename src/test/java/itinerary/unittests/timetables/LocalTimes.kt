package itinerary.unittests.timetables

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors


object LocalTimes {
    fun at(vararg times: String): List<LocalTime> =
        times.map{LocalTime.parse(it, DateTimeFormatter.ofPattern("H:mm"))}
}