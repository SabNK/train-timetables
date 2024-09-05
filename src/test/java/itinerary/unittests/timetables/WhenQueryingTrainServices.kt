package itinerary.unittests.timetables

import itinerary.InMemoryTimeTable
import itinerary.unittests.timetables.LocalTimes.at
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("When querying train services")
class WhenQueryingTrainServices {
    // Given
    val timeTable = InMemoryTimeTable()

    @Test
    @DisplayName("We can ask which lines go through any two stations")
    fun queryLinesThroughStations() {
        // When
        timeTable.scheduleService(
            "T1",
            at("09:15"),
            "Hornsby",
            "Central"
        )
        // Then
        assertThat(timeTable.findLinesThrough("Hornsby", "Central")).hasSize(1)
    }
}