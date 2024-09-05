package itinerary.unittests.itinararies

import itinerary.CustomTimeTable
import itinerary.ItineraryService
import itinerary.TimeTable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@DisplayName("When finding the next departure times")
class WhenFindingNextDepartureTimes {
    @Test
    @DisplayName("должен вернуть первый поезд отправление после заданного времени")
    fun tripWithOneScheduledTime() {
        //Given
        val timeTable = departures(at("8:10"), at("8:20"), at("8:30"),)
        val itineraryService = ItineraryService(timeTable)
        // When
        val proposedDepartures = itineraryService
            .findNextDepartures(at("8:25"), "Hornsby", "Central")
        // Then
        assertThat(proposedDepartures).containsExactly(at("8:30"))
    }
    @Test
    @DisplayName("должен предложить два поезда")
    fun tripWithSeveralScheduledTime() {
        //Given
        val timeTable = departures(at("8:10"), at("8:20"), at("8:30"),at("8:45"),)
        val itineraryService = ItineraryService(timeTable)
        // When
        val proposedDepartures = itineraryService
            .findNextDepartures(at("8:05"), "Hornsby", "Central")
        // Then
        assertThat(proposedDepartures).containsExactly(at("8:10"), at("8:20"),)
    }


    @Test
    @DisplayName("должен вернуть пустой список если поездов нет")
    fun anAfterHourTrip() {
        //Given
        val timeTable = departures(at("8:10"), at("8:20"), at("8:30"))
        val itineraryService = ItineraryService(timeTable)
        // When
        val proposedDepartures = itineraryService
            .findNextDepartures(at("8:50"), "Hornsby", "Central")
        // Then
        assertThat(proposedDepartures).isEmpty()
    }

    private fun departures(vararg departures: LocalTime): TimeTable {
        return CustomTimeTable(departures.toList())
    }
}

private fun at(time: String): LocalTime {
    return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"))
}