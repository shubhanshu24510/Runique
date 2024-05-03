package com.shubhans.core.domain.utils

sealed interface DataError:Error {
    enum class NetworkError:DataError{
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN

    }
    enum class LocalError:DataError{
        DISK_FULL
    }

}