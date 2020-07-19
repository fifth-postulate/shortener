package nl.fifthpostulate.shortener.result

sealed class Result<Error, Value> {}

class Success<Error, Value>(val data: Value): Result<Error, Value>() {}
class Failure<Error, Value>(val error: Error): Result<Error, Value>() {}
