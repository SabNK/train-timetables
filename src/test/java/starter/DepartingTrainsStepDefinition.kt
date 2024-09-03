package starter

import io.cucumber.java.ParameterType
import io.cucumber.java.PendingException
import io.cucumber.java.ru.Дано
import io.cucumber.java.ru.Если
import io.cucumber.java.ru.То
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DepartingTrainsStepDefinition {
    @Дано("ближайшие поезда в {station} по {line} отправятся из {station} в {times}")
    fun `отправление ближайших поездов`(
        line: String,
        to: String,
        from: String,
        departureTimes: String
    ) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @Если("Трэвис решит поехать из {} в {} в {}")
    fun travel(
        from: String,
        to: String,
        departureTime: String
    ) {
        // Write code here that turns the phrase above into concrete actions
        throw PendingException()
    }

    @То("сообщить ему, что поезда отправятся в {times}")
    fun shouldBeToldAboutTrainsAt(expectedTimes: List<LocalTime>) {
        assertThat(proposedDepartures).isEqualTo(expectedTimes)
    }

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