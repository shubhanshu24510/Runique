package com.shubhans.core.presentation.ui

import androidx.compose.ui.res.stringResource
import com.shubhans.core.domain.utils.DataError
import com.shubhans.core.presentation.ui.UiText

fun DataError.asUiText(): UiText {
    return when(this) {
        DataError.LocalError.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )
        DataError.NetworkError.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.error_request_timeout
        )
        DataError.NetworkError.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.error_too_many_requests
        )
        DataError.NetworkError.NO_INTERNET -> UiText.StringResource(
            R.string.error_no_internet
        )
        DataError.NetworkError.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.error_payload_too_large
        )
        DataError.NetworkError.SERVER_ERROR -> UiText.StringResource(
            R.string.error_server_error
        )
        DataError.NetworkError.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )
        else -> UiText.StringResource(R.string.error_unknown)
    }
}