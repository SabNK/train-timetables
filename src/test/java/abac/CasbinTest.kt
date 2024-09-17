package abac

import com.googlecode.aviator.runtime.function.AbstractFunction
import com.googlecode.aviator.runtime.function.FunctionUtils
import com.googlecode.aviator.runtime.type.AviatorBoolean
import com.googlecode.aviator.runtime.type.AviatorNumber
import com.googlecode.aviator.runtime.type.AviatorObject
import org.assertj.core.api.Assertions.assertThat
import org.casbin.jcasbin.main.Enforcer
import org.casbin.jcasbin.util.function.CustomFunction
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths


@DisplayName("Tests for access control")
class CasbinTest {

    val res = javaClass.classLoader.getResource("abacs")
    val folder = Paths.get(res.toURI()).toAbsolutePath().toString()
    val e = Enforcer("$folder/model.conf", "$folder/policy.csv")
    init{
        e.addFunction(Distance.name, Distance())
    }

    @Test
    @DisplayName("должен разрешить алисе")
    fun correct() {
        val sub = "alice"
        val obj = "data1"
        val act = "read"
        val lat1 = ""
        val lon1 = ""
        val lat2 = ""
        val lon2 = ""
        assertThat(e.enforce(sub, obj, act, lat1, lon1, lat2, lon2)).isTrue()
    }
    @Test
    @DisplayName("должен запретить бобу")
    fun wrong() {
        val sub = "bob"
        val obj = "data2"
        val act = "read"
        val lat1 = "ab"
        val lon1 = "ab"
        val lat2 = "ab"
        val lon2 = "ab"
        assertThat(e.enforce(sub, obj, act, lat1, lon1, lat2, lon2)).isFalse()
    }
    @Test
    @DisplayName("должен разрешить бобу по координатам")
    fun correct_by_lat() {
        val sub = "bob"
        val obj = "data2"
        val act = "read"
        val lat1 = "53.33333"
        val lon1 = "ab"
        val lat2 = "ab"
        val lon2 = "ab"
        assertThat(e.enforce(sub, obj, act, lat1, lon1, lat2, lon2)).isFalse()
    }

}

fun distance(lat1: String, lon1: String, lat2: String, lon2: String): Int =
    listOf(lat1, lon1, lat2, lon2).fold(0){ acc, element -> acc + element.length }

class Distance(): CustomFunction() /*AbstractFunction()*/ {
    companion object{
        val name: String
            get() = "distance"
    }
    override fun call(
        env: Map<String, Any>,
        arg1: AviatorObject,
        arg2: AviatorObject,
        arg3: AviatorObject,
        arg4: AviatorObject
    ): AviatorObject {
        val lat1: String = FunctionUtils.getStringValue(arg1, env)
        val lon1: String = FunctionUtils.getStringValue(arg2, env)
        val lat2: String = FunctionUtils.getStringValue(arg3, env)
        val lon2: String = FunctionUtils.getStringValue(arg4, env)

        println("lat1 $lat1, lon1 $lon1, lat2 $lat2, lon2 $lon2")
        return AviatorNumber.valueOf(distance(lat1, lon1, lat2, lon2))
    }

    override fun getName(): String = Distance.name
}


/*
data class Group(val name: String, val groupId: Int, val rights: List<TypeBasedRights>)

enum class PolescannerFeature{
    POLE,
    GRID,
    ATTACH,
    ELECTRIC
}

//admin sets
data class TypeBasedRights(val entityClassName: String, val locationActions: Set<LocationAction>)

data class LocationAction (val action: Actions, val distancesM: Map<Actions, Int>) {
    fun best(other: LocationAction): LocationAction {
        if (other.action != action) return this
        return LocationAction(action, distancesM.maximize(other.distancesM))
    }
}

fun Map<Actions, Int>.maximize(other: Map<Actions, Int>): Map<Actions, Int> {
    val result = mutableMapOf<Actions, Int>()
    this.entries.forEach{e ->
        if (other[e.key] != null && other[e.key]!! > e.value)
            result[e.key] = other[e.key]!!
        else
            result[e.key] = this[e.key]!!
    }
    return result
}


data class LocationRights(val action: Actions, val center: Point, val distanceM: Int)
data class IDRights(val entityClassName: String, val id: UUID, val actions: Set<Actions>)


enum class Actions {
    CREATE,
    READ,
    UPDATE,
    DELETE
}




data class DistanceRights(val entityClassName: String, val polygon: List<String>)

fun Account.registerPole(pole: Pole){
    val right = groups.typeBasedRights.firstOrNull { it.entityClassName == "Pole" }
    if (right != null && Actions.CREATE in right.actions) {
        val uuid = pole.register()
        this.idBasedRights.add(IDRights("Pole", uuid, right.actions))
        this.locationRights.add(pole.geo)
    }
}





fun Pole.register() = UUID.randomUUID()

*/
