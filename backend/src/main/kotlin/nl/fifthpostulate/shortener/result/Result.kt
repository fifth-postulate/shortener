package nl.fifthpostulate.shortener.result

sealed class Result<Error, Value>(val type: String) {
    fun withDefault(defaultValue: Value): Value {
        return when (this) {
            is Success -> this.data
            is Failure -> defaultValue
        }
    }

    fun ignore(): Result<Error, Unit> {
        return when (this) {
            is Success -> Success(Unit)
            is Failure -> Failure(error)
        }

    }
}

class Success<Error, Value>(val data: Value) : Result<Error, Value>("success")
class Failure<Error, Value>(val error: Error) : Result<Error, Value>("failure")
