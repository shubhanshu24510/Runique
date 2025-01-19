package com.shubhans.runique

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Auth : Route

    @Serializable
    data object Intro : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Run : Route

    @Serializable
    data object RunOverview : Route

    @Serializable
    data object ActiveRun : Route
}