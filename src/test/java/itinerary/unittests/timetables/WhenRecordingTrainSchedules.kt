package itinerary.unittests.timetables

import itinerary.InMemoryTimeTable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Когда сервис планирования поездов")
class WhenRecordingTrainSchedules {
    // Given
    val timeTable = InMemoryTimeTable()

    @Test
    @DisplayName("We can schedule a trip with a single scheduled time")
    fun tripWithOneScheduledTime() {
        // When
        timeTable.scheduleService(
            "T1",
            LocalTimes.at("09:15"),
            "Hornsby",
            "Central"
        )
        // Then
        assertThat(timeTable.getDepartures("T1", "Hornsby")).hasSize(1)
    }

}