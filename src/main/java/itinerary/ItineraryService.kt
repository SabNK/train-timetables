package itinerary

import java.time.LocalTime

data class ItineraryService(val timetable: TimeTable) {
    fun findNextDepartures(
        departureTime: LocalTime,
        from: String,
        to: String
    ): List<LocalTime> {
        val lines = timetable.findLinesThrough(from, to)

        return lines
            .flatMap { timetable.getDepartures("", it) }
            .filter{ it.isAfter(departureTime)}
            .sorted()
            .take(2)
    }
}
interface TimeTable {
    fun findLinesThrough(from: String, to: String): List<String>
    fun getDepartures(line: String, station: String): Iterable<LocalTime>
}

class CustomTimeTable(val departures: List<LocalTime>): TimeTable {
    override fun findLinesThrough(from: String, to: String): List<String> {
        return listOf("T1")
    }

    override fun getDepartures(line: String, station: String): Iterable<LocalTime> {
        return departures
    }
}

class InMemoryTimeTable: TimeTable, CanScheduleServices{
    val schedules = HashMap<String, ScheduledService>()
    override fun findLinesThrough(from: String, to: String): List<String> {
        return schedules.entries.filter { with(it.value){departure == from && destination == to}}
            .map{ it.key}
    }

    override fun getDepartures(line: String, station: String): Iterable<LocalTime> {
        return emptyList()
    }

    override fun scheduleService(line: String, departingAt: List<LocalTime>, departure: String, destination: String) {
        schedules.put(line, ScheduledService(departure, destination, departingAt))
    }

}

data class ScheduledService(val departure: String, val destination: String, val departingAt: List<LocalTime>) {

}


interface CanScheduleServices {
    fun scheduleService(
        line: String,
        departingAt: List<LocalTime>,
        departure: String,
        destination: String
    )
}

