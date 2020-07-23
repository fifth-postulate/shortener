package nl.fifthpostulate.shortener.result

sealed class Result<Error, Value>(val type: String) {
    fun withDefault(defaultValue: Value): Value {
        return when (this) {
            is Success -> data
            is Failure -> defaultValue
        }
    }

    fun <T> andThen(transform : (Value) -> Result<Error, T>): Result<Error, T> {
        return when (this) {
            is Success -> transform(data)
            is Failure -> Failure(error)
        }
    }

    fun <T> map(transform: (Value) -> T): Result<Error, T> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(error)
        }
    }

    fun <T> mapError(transform: (Error) -> T): Result<T, Value> {
        return when (this) {
            is Success -> Success(data)
            is Failure -> Failure(transform(error))
        }
    }

    fun use(actOn: (Value) -> Unit): Result<Error, Value> {
        when (this) {
            is Success -> actOn(data)
            is Failure -> Unit
        }
        return this
    }
}

class Success<Error, Value>(val data: Value) : Result<Error, Value>("success")
class Failure<Error, Value>(val error: Error) : Result<Error, Value>("failure")
