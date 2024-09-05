package itinerary.acceptancetests.steps

import io.cucumber.java.PendingException
import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.Если
import io.cucumber.java.ru.То
import itinerary.InMemoryTimeTable
import itinerary.ItineraryService
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalTime


class DepartingTrainsStepDefinition {
    lateinit var proposedDepartures: List<LocalTime>
    lateinit var itinaryService: ItineraryService
    val timeTable = InMemoryTimeTable()

    @Дано("ближайшие поезда в {station} по {line} отправятся из {station} в {departureTimes}")
    fun `отправление ближайших поездов`(
        line: String,
        to: String,
        from: String,
        departureTimes: List<LocalTime>
    ) {
        timeTable.scheduleService(line, departureTimes, from, to)
    }

    @Если("Трэвис решит поехать из {} в {} в {}")
    fun travel(
        from: String,
        to: String,
        departureTime: LocalTime
    ) {
        proposedDepartures = itinaryService.findNextDepartures(departureTime, from, to)
        throw PendingException()
    }

    @То("сообщить ему, что поезда отправятся в {times}")
    fun shouldBeToldAboutTrainsAt(expectedTimes: List<LocalTime>) {
        assertThat(proposedDepartures).isEqualTo(expectedTimes)
    }
}