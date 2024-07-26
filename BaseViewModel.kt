package com.antares.fleetservice.viewmodel

import androidx.lifecycle.ViewModel
import com.antares.fleetservice.service.FleetServiceRepository

abstract class BaseViewModel internal constructor() : ViewModel() {
    internal var fleetRepository = FleetServiceRepository.getInstance()
}