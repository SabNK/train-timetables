package abac

import org.assertj.core.api.Assertions.assertThat
import org.casbin.jcasbin.main.Enforcer
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths

@DisplayName("Tests for access control")
class CasbinTest {

    val res = javaClass.classLoader.getResource("abacs")
    val folder = Paths.get(res.toURI()).toAbsolutePath().toString()
    val e = Enforcer("$folder/model.conf", "$folder/policy.csv")

    @Test
    @DisplayName("должен разрешить алисе")
    fun correct() {
        val sub = "alice"
        val obj = "data1"
        val act = "read"
        assertThat(e.enforce(sub, obj, act)).isTrue()
    }
    @Test
    @DisplayName("должен запретить бобу")
    fun wrong() {
        val sub = "bob"
        val obj = "data2"
        val act = "read"
        assertThat(e.enforce(sub, obj, act)).isFalse()
    }
}