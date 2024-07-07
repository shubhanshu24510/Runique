package com.shubhans.connectivity.domain.messageing

import com.shubhans.core.domain.utils.Error

enum class MessagingError:Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}