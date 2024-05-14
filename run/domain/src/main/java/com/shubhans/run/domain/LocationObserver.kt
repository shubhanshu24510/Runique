package com.shubhans.run.domain

import com.shubhans.core.domain.location.LocationWIthAltitute
import kotlinx.coroutines.flow.Flow

interface LocationObserver{
    fun observerLocation(interval:Long):Flow<LocationWIthAltitute>
}
