package nl.fifthpostulate.shortener.result

sealed class Result<Error, Value>(val type: String) {}

class Success<Error, Value>(val data: Value): Result<Error, Value>("success") {}
class Failure<Error, Value>(val error: Error): Result<Error, Value>("failure") {}
