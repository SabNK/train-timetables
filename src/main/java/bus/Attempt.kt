package bus

import com.trendyol.kediatr.*
import kotlinx.coroutines.runBlocking
import org.casbin.jcasbin.main.Enforcer
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap

class ManualDependencyProvider(
    private val handlerMap: HashMap<Class<*>, Any>
) : DependencyProvider {
    override fun <T> getSingleInstanceOf(clazz: Class<T>): T {
        return handlerMap[clazz] as T
    }

    override fun <T> getSubTypesOf(clazz: Class<T>): Collection<Class<T>> {
        return handlerMap
            .filter { it.key.interfaces.contains(clazz) }
            .map { it.key as Class<T> }
    }
}

fun main() {
    val handlers: HashMap<Class<*>, Any> = hashMapOf(
        PingCommandHandler::class.java to PingCommandHandler(),
        PingQueryHandler::class.java to PingQueryHandler(),
        MeasurePipelineBehaviour::class.java to MeasurePipelineBehaviour(),
        HaHaHaPipelineBehaviour::class.java to HaHaHaPipelineBehaviour(),
        PingNotification::class.java to PingNotificationHandler(),
    )

    val provider = ManualDependencyProvider(handlers)
    val mediator: Mediator = MediatorBuilder(provider).build()
    runBlocking {
        mediator.send(PingCommand()) // 1..1
        mediator.send(PingQuery()) // 1..1
        mediator.publish(PingNotification()) // 0..N

    }


}
val res = javaClass.classLoader.getResource("abacs2")
val folder = Paths.get(res.toURI()).toAbsolutePath().toString()
val e = Enforcer("$folder/model.conf", "$folder/policy.csv")
val userId = UUID.fromString("123")

data class ABACObject(val entityId: UUID, val attributes: List<String>)

abstract class PrivelegedCommand(val obj: ABACObject, val action: CRUDOperation): Command

enum class CRUDOperation {
CREATE, READ, UPDATE, DELETE
}

class UpdatePoleAttributeCmd(
    obj: ABACObject,
    val newValue: Any,
    action: CRUDOperation = CRUDOperation.UPDATE): PrivelegedCommand(obj, action)

class UpdatePoleAttributeCmdHandler: CommandHandler<UpdatePoleAttributeCmd> {
    override suspend fun handle(command: UpdatePoleAttributeCmd) {
        println("updated")
    }
}


class PermissionPipelineBehaviour: PipelineBehavior  {
    override suspend fun <TRequest, TResponse> handle(
        request: TRequest,
        next: RequestHandlerDelegate<TRequest, TResponse>
    ): TResponse {
        fun checkPriveleges(sub: UUID, obj: ABACObject, act: CRUDOperation): Boolean{
            return e.enforce(sub.toString(), obj.toString(), act.toString()) //ToDo Wrap On enforce!
        }

        val proceed = when (request) {
            is PrivelegedCommand -> checkPriveleges(userId, request.obj, request.action)
            else -> true
        }
        val response = if (proceed) next(request) else notification

        return response
    }
}

data class PingCommand(val pingId: UUID  ): Command // or
class PingQuery: Query<String> // or
class PingNotification: Notification
class PingCommandHandler: CommandHandler<PingCommand> {
    override suspend fun handle(command: PingCommand) {
        println("Pong! 1")
    }
}
class PingQueryHandler: QueryHandler<PingQuery, String> {
    override suspend fun handle(query: PingQuery): String {
        println("Pong! 2")
        return "Pong! 2"
    }
}

class PingNotificationHandler: NotificationHandler<PingNotification> {
    override suspend fun handle(notification: PingNotification) {
        println("Pong! 3")
    }
}

class MeasurePipelineBehaviour: PipelineBehavior  {
    override suspend fun <TRequest, TResponse> handle(
        request: TRequest,
        next: RequestHandlerDelegate<TRequest, TResponse>
    ): TResponse {
        val start = System.currentTimeMillis()
        val response = next(request)
        val end = System.currentTimeMillis()
        println("Request ${request!!::class.simpleName} took ${end - start} ms")
        return response
    }
}

class HaHaHaPipelineBehaviour: PipelineBehavior  {
    override suspend fun <TRequest, TResponse> handle(
        request: TRequest,
        next: RequestHandlerDelegate<TRequest, TResponse>
    ): TResponse {
        println("HaHaHa!")
        val response = next(request)
        return response
    }
}

interface IPipeline1

class FooBehavior<TRequest, TResponse> : PipelineBehavior<TRequest, TResponse> { }
