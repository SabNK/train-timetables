package itinerary.acceptancetests.steps

import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.Если
import io.cucumber.java.ru.То
import itinerary.InMemoryTimeTable
import itinerary.ItineraryService
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalTime


class DepartingTrainsStepDefinition {
    lateinit var proposedDepartures: List<LocalTime>

    val timeTable = InMemoryTimeTable()
    val itinaryService: ItineraryService = ItineraryService(timeTable)

    @Дано("ближайшие поезда в {station} по {line} отправляются из {station} в {departureTimes}")
    fun `отправление ближайших поездов`(
        to: String,
        line: String,
        from: String,
        departureTimes: List<LocalTime>
    ) {
        //print("line $line: from $from, to $to")
        timeTable.scheduleService(line, departureTimes, from, to)
    }

    @Если("Трэвис решит поехать из {station} в {station} в {departureTime}")
    fun travel(
        from: String,
        to: String,
        departureTime: LocalTime
    ) {
        //print("2 line: from $from, to $to")
        proposedDepartures = itinaryService.findNextDepartures(departureTime, from, to)
    }

    @То("сообщить ему, что поезда отправятся в {departureTimes}")
    fun shouldBeToldAboutTrainsAt(expectedDepartures: List<LocalTime>) {
        assertThat(proposedDepartures).isEqualTo(expectedDepartures)
    }
}