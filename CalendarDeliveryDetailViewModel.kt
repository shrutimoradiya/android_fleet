package com.antares.fleetservice.viewmodel

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.antares.fleetservice.model.calendarDeliveryDetail.CalendarDeliveryDetailEvents
import com.antares.fleetservice.utils.Constant

class CalendarDeliveryDetailViewModel : BaseViewModel() {

    internal var lockData = MutableLiveData<ArrayList<CalendarDeliveryDetailEvents>>()

    fun getCalendarDeliveryDetail(date: String, id: Int) {

        val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
        map["created_from"] = Constant.createFrom
        map["date"] = date
        map["ids[]"] = id

        fleetRepository?.getCalendarDelieveryDetail(map)?.observeForever { it ->
            it?.let { it1 ->
                it1.json?.events?.let {
                    lockData.value = it
                }
            }
        }
    }

    fun postCalendarDeliveryDetail(map: ArrayMap<String?, Any?>?, date: String, id: Int) {
        fleetRepository?.postCalendarDelieveryDetail(map = map)?.observeForever {
            getCalendarDeliveryDetail(date = date, id = id)
        }
    }
}
