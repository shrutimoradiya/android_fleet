package com.antares.fleetservice.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.antares.fleetservice.model.mechanic.home.*
import com.antares.fleetservice.utils.Constant
import java.util.*
import kotlin.collections.ArrayList

class MechanicHomeDB(context: Context?) : SQLiteOpenHelper(context, "MechanicHome.db", null, 1) {

    // table name
    private val mechanicHome = "MechanicHome"
    private val eventsData: String = "Events"
    private val locationDetail = "LocationDetail"
    private val carDetail = "CarDetail"
    private val history = "History"
    private val subEvent = "SubEvent"

    // row
    private val events: String = "events"
    private val mobileStationLocationDetail: String = "mobileStationLocationDetail"
    private val mobileStationCarDetail: String = "mobileStationCarDetail"
    private val dayCloseHistory: String = "dayCloseHistory"
    private val date: String = "date"

    private val id: String = "id"
    private val carId: String = "carId"
    private val displayId: String = "displayId"
    private val name: String = "name"
    private val address: String = "address"
    private val state: String = "state"
    private val postalCode: String = "postalCode"
    private val lat: String = "lat"
    private val lon: String = "lon"
    private val customerId: String = "customerId"
    private val locationType: String = "locationType"
    private val parentId: String = "parentId"
    private val officeIcon: String = "officeIcon"
    private val calendarColor: String = "calendarColor"
    private val partnerName: String = "partnerName"
    private val deleted: String = "deleted"
    private val createdAt: String = "createdAt"
    private val updatedAt: String = "updatedAt"
    private val latitude: String = "latitude"
    private val longitude: String = "longitude"
    private val mslid: String = "mslid"

    private val numberPlate: String = "numberPlate"
    private val vehicleVersion: String = "vehicleVersion"
    private val year: String = "year"
    private val registrationYear: String = "registrationYear"
    private val month: String = "month"
    private val makeId: String = "makeId"
    private val modelId: String = "modelId"
    private val bodyType: String = "bodyType"
    private val totalDoors: String = "totalDoors"
    private val fuelType: String = "fuelType"
    private val powerType: String = "powerType"
    private val transmissionType: String = "transmissionType"
    private val totalKm: String = "totalKm"
    private val enterKm: String = "enterKm"
    private val locationId: String = "locationId"
    private val euroClass: String = "euroClass"
    private val carColor: String = "carColor"
    private val ownerType: String = "ownerType"
    private val ownerId: String = "ownerId"
    private val isMobileStation: String = "isMobileStation"
    private val carTypology: String = "carTypology"
    private val carType: String = "carType"
    private val ledXeon: String = "ledXeon"
    private val navigationSystem: String = "navigationSystem"
    private val width: String = "width"
    private val ratio: String = "ratio"
    private val diameter: String = "diameter"
    private val vin: String = "vin"
    private val dateInstallation: String = "dateInstallation"
    private val fatt: String = "fatt"
    private val makeName: String = "makeName"
    private val businessName: String = "businessName"
    private val locationName: String = "locationName"
    private val locationAddress: String = "locationAddress"
    private val modelName: String = "modelName"

    private val mobileStationId: String = "mobileStationId"
    private val closedBy: String = "closedBy"

    private val parentWorkPlanId: String = "parentWorkPlanId"
    private val isEmailSend: String = "isEmailSend"
    private val emailSentDate: String = "emailSentDate"
    private val createdBy: String = "createdBy"
    private val icon: String = "icon"
    private val statusId: String = "statusId"
    private val orderStatusId: String = "orderStatusId"
    private val inchargeId: String = "inchargeId"
    private val isOrderNew: String = "isOrderNew"
    private val isCreatedByMechanic: String = "isCreatedByMechanic"
    private val isNew: String = "isNew"
    private val isRead: String = "isRead"
    private val isMessageRead: String = "isMessageRead"
    private val isReportConfirm: String = "isReportConfirm"
    private val configuredBy: String = "configuredBy"
    private val configuredByLocationId: String = "configuredByLocationId"
    private val note: String = "note"
    private val customerName: String = "customerName"
    private val createdByName: String = "createdByName"
    private val inchargeName: String = "inchargeName"
    private val statusName: String = "statusName"
    private val leadCount: String = "leadCount"
    private val customerWorkPlanId: String = "customerWorkPlanId"
    private val isTimeDisplay: String = "isTimeDisplay"
    private val travelStartTime: String = "travelStartTime"
    private val workStartTime: String = "workStartTime"
    private val subEvents: String = "subEvents"
    private val subEventId: String = "subEventId"

    private val title: String = "title"
    private val start: String = "start"
    private val end: String = "end"
    private val travelStart: String = "travelStart"
    private val travelEnd: String = "travelEnd"
    private val backgroundColor: String = "backgroundColor"
    private val officeCalendarColor: String = "officeCalendarColor"
    private val leadId: String = "leadId"
    private val orderId: String = "orderId"
    private val userId: String = "userId"
    private val userName: String = "userName"
    private val description: String = "description"
    private val actionDate: String = "actionDate"
    private val title1: String = "title1"
    private val objectType: String = "objectType"
    private val contactId: String = "contactId"
    private val firstName: String = "firstName"
    private val lastName: String = "lastName"
    private val email: String = "email"
    private val allDay: String = "allDay"
    private val completed: String = "completed"
    private val mobile: String = "mobile"
    private val leadTypeId: String = "leadTypeId"
    private val priority: String = "priority"
    private val currentStep: String = "currentStep"
    private val isClosed: String = "isClosed"
    private val isWorkNotDone: String = "isWorkNotDone"
    private val isDayClose: String = "isDayClose"
    private val calendarWorkPlanId: String = "calendarWorkPlanId"
    private val calendarWorkDisplayId: String = "calendarWorkDisplayId"
    private val workEndTime: String = "workEndTime"
    private val travelTime: String = "travelTime"
    private val mechanicWorkStartTime: String = "mechanicWorkStartTime"
    private val mechanicWorkEndTime: String = "mechanicWorkEndTime"
    private val mechanicTravelStartTime: String = "mechanicTravelStartTime"
    private val mechanicTravelEndTime: String = "mechanicTravelEndTime"
    private val isEventStart: String = "isEventStart"
    private val sourceAddress: String = "sourceAddress"
    private val destinationAddress: String = "destinationAddress"
    private val sedeName: String = "sedeName"
    private val closedAt: String = "closedAt"
    private val dropAddress: String = "dropAddress"
    private val dropLat: String = "dropLat"
    private val dropLon: String = "dropLon"
    private val towRequestAccepted: String = "towRequestAccepted"
    private val isPicked: String = "isPicked"
    private val isTravelCompleted: String = "isTravelCompleted"
    private val mobileStationLat: String = "mobileStationLat"
    private val mobileStationLon: String = "mobileStationLon"
    private val isManualReport: String = "isManualReport"
    private val isPauseTravel: String = "isPauseTravel"
    private val mechanicTravelBreakStart: String = "mechanicTravelBreakStart"
    private val mechanicTravelBreakEnd: String = "mechanicTravelBreakEnd"
    private val mechanicTravelRestart: String = "mechanicTravelRestart"
    private val isNoteRead: String = "isNoteRead"
    private val customerWorkPlanStatusId: String = "customerWorkPlanStatusId"
    private val isOtherVehicleBtn: String = "isOtherVehicleBtn"
    private val isCustomerWorkPlanNoteReadBtn: String = "isCustomerWorkPlanNoteReadBtn"
    private val isPrintPdfBtn: String = "isPrintPdfBtn"
    private val workHours: String = "workHours"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "create table "
                    + mechanicHome + " ("
                    + events + " TEXT, "
                    + mobileStationLocationDetail + " TEXT, "
                    + mobileStationCarDetail + " TEXT, "
                    + dayCloseHistory + " TEXT, "
                    + date + " TEXT)"
        )

        db?.execSQL(
            "create table "
                    + locationDetail + " ("
                    + id + " TEXT, "
                    + carId + " TEXT, "
                    + displayId + " TEXT, "
                    + name + " TEXT, "
                    + address + " TEXT, "
                    + state + " TEXT, "
                    + postalCode + " TEXT, "
                    + lat + " TEXT, "
                    + lon + " TEXT, "
                    + customerId + " TEXT, "
                    + locationType + " TEXT, "
                    + parentId + " TEXT, "
                    + officeIcon + " TEXT, "
                    + calendarColor + " TEXT, "
                    + partnerName + " TEXT, "
                    + deleted + " TEXT, "
                    + createdAt + " TEXT, "
                    + updatedAt + " TEXT, "
                    + latitude + " TEXT, "
                    + longitude + " TEXT, "
                    + mslid + " TEXT, "
                    + date + " TEXT)"
        )

        db?.execSQL(
            "create table "
                    + carDetail + " ("
                    + id + " TEXT, "
                    + numberPlate + " TEXT, "
                    + vehicleVersion + " TEXT, "
                    + year + " TEXT, "
                    + registrationYear + " TEXT, "
                    + month + " TEXT, "
                    + makeId + " TEXT, "
                    + modelId + " TEXT, "
                    + bodyType + " TEXT, "
                    + totalDoors + " TEXT, "
                    + fuelType + " TEXT, "
                    + powerType + " TEXT, "
                    + transmissionType + " TEXT, "
                    + totalKm + " TEXT, "
                    + enterKm + " TEXT, "
                    + customerId + " TEXT, "
                    + locationId + " TEXT, "
                    + euroClass + " TEXT, "
                    + carColor + " TEXT, "
                    + ownerType + " TEXT, "
                    + ownerId + " TEXT, "
                    + isMobileStation + " TEXT, "
                    + carTypology + " TEXT, "
                    + carType + " TEXT, "
                    + ledXeon + " TEXT, "
                    + navigationSystem + " TEXT, "
                    + width + " TEXT, "
                    + ratio + " TEXT, "
                    + diameter + " TEXT, "
                    + vin + " TEXT, "
                    + dateInstallation + " TEXT, "
                    + fatt + " TEXT, "
                    + deleted + " TEXT, "
                    + createdAt + " TEXT, "
                    + updatedAt + " TEXT, "
                    + makeName + " TEXT, "
                    + businessName + " TEXT, "
                    + locationName + " TEXT, "
                    + locationAddress + " TEXT, "
                    + lat + " TEXT, "
                    + lon + " TEXT, "
                    + modelName + " TEXT, "
                    + date + " TEXT)"
        )

        db?.execSQL(
            "create table "
                    + history + " ("
                    + id + " TEXT, "
                    + date + " TEXT, "
                    + mobileStationId + " TEXT, "
                    + closedBy + " TEXT, "
                    + createdAt + " TEXT, "
                    + updatedAt + " TEXT)"
        )

        db?.execSQL(
            "create table "
                    + eventsData + " ("
                    + id + " TEXT, "
                    + displayId + " TEXT, "
                    + parentWorkPlanId + " TEXT, "
                    + customerId + " TEXT, "
                    + mobileStationId + " TEXT, "
                    + locationId + " TEXT, "
                    + date + " TEXT, "
                    + lat + " TEXT, "
                    + lon + " TEXT, "
                    + address + " TEXT, "
                    + isEmailSend + " TEXT, "
                    + emailSentDate + " TEXT, "
                    + createdBy + " TEXT, "
                    + icon + " TEXT, "
                    + statusId + " TEXT, "
                    + orderStatusId + " TEXT, "
                    + inchargeId + " TEXT, "
                    + isOrderNew + " TEXT, "
                    + isCreatedByMechanic + " TEXT, "
                    + isNew + " TEXT, "
                    + isRead + " TEXT, "
                    + calendarColor + " TEXT, "
                    + isMessageRead + " TEXT, "
                    + isReportConfirm + " TEXT, "
                    + configuredBy + " TEXT, "
                    + configuredByLocationId + " TEXT, "
                    + note + " TEXT, "
                    + deleted + " TEXT, "
                    + createdAt + " TEXT, "
                    + updatedAt + " TEXT, "
                    + customerName + " TEXT, "
                    + createdByName + " TEXT, "
                    + inchargeName + " TEXT, "
                    + statusName + " TEXT, "
                    + locationName + " TEXT, "
                    + leadCount + " TEXT, "
                    + customerWorkPlanId + " TEXT, "
                    + isTimeDisplay + " TEXT, "
                    + travelStartTime + " TEXT, "
                    + workStartTime + " TEXT, "
                    + subEvents + " TEXT, "
                    + subEventId + " TEXT)"
        )

        db?.execSQL(
            "create table "
                    + subEvent + " ("
                    + id + " TEXT, "
                    + title + " TEXT, "
                    + start + " TEXT, "
                    + end + " TEXT, "
                    + travelStart + " TEXT, "
                    + travelEnd + " TEXT, "
                    + numberPlate + " TEXT, "
                    + backgroundColor + " TEXT, "
                    + officeCalendarColor + " TEXT, "
                    + leadId + " TEXT, "
                    + address + " TEXT, "
                    + orderId + " TEXT, "
                    + userId + " TEXT, "
                    + userName + " TEXT, "
                    + description + " TEXT, "
                    + actionDate + " TEXT, "
                    + title1 + " TEXT, "
                    + objectType + " TEXT, "
                    + contactId + " TEXT, "
                    + firstName + " TEXT, "
                    + lastName + " TEXT, "
                    + email + " TEXT, "
                    + allDay + " TEXT, "
                    + completed + " TEXT, "
                    + mobile + " TEXT, "
                    + leadTypeId + " TEXT, "
                    + priority + " TEXT, "
                    + carId + " TEXT, "
                    + mobileStationId + " TEXT, "
                    + businessName + " TEXT, "
                    + locationName + " TEXT, "
                    + currentStep + " TEXT, "
                    + isClosed + " TEXT, "
                    + isWorkNotDone + " TEXT, "
                    + isDayClose + " TEXT, "
                    + calendarWorkPlanId + " TEXT, "
                    + calendarWorkDisplayId + " TEXT, "
                    + workStartTime + " TEXT, "
                    + workEndTime + " TEXT, "
                    + totalKm + " TEXT, "
                    + travelTime + " TEXT, "
                    + mechanicWorkStartTime + " TEXT, "
                    + mechanicWorkEndTime + " TEXT, "
                    + mechanicTravelStartTime + " TEXT, "
                    + mechanicTravelEndTime + " TEXT, "
                    + isEventStart + " TEXT, "
                    + sourceAddress + " TEXT, "
                    + destinationAddress + " TEXT, "
                    + sedeName + " TEXT, "
                    + closedAt + " TEXT, "
                    + enterKm + " TEXT, "
                    + lat + " TEXT, "
                    + lon + " TEXT, "
                    + dropAddress + " TEXT, "
                    + dropLat + " TEXT, "
                    + dropLon + " TEXT, "
                    + towRequestAccepted + " TEXT, "
                    + isPicked + " TEXT, "
                    + isTravelCompleted + " TEXT, "
                    + displayId + " TEXT, "
                    + mobileStationLat + " TEXT, "
                    + mobileStationLon + " TEXT, "
                    + isManualReport + " TEXT, "
                    + isPauseTravel + " TEXT, "
                    + mechanicTravelBreakStart + " TEXT, "
                    + mechanicTravelBreakEnd + " TEXT, "
                    + mechanicTravelRestart + " TEXT, "
                    + customerWorkPlanId + " TEXT, "
                    + isReportConfirm + " TEXT, "
                    + makeName + " TEXT, "
                    + modelName + " TEXT, "
                    + note + " TEXT, "
                    + isNoteRead + " TEXT, "
                    + customerWorkPlanStatusId + " TEXT, "
                    + isOtherVehicleBtn + " TEXT, "
                    + isTimeDisplay + " TEXT, "
                    + isCustomerWorkPlanNoteReadBtn + " TEXT, "
                    + isPrintPdfBtn + " TEXT, "
                    + workHours + " TEXT, "
                    + subEventId + " TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("oldVersion = $oldVersion | newVersion = $newVersion")
    }

    fun addData(data: MechanicHomeJson, date1: Calendar, context: Context): Boolean {

        for (temp in data.events) {
            temp.detail?.let {
                addEvent(it, date1, context)
            }
        }

        data.mobileStationLocationDetail?.let {
            addLocationDetail(it, date1)
        }

        data.mobileStationCarDetail?.let {
            addCarDetail(it, date1)
        }

        data.dayCloseHistory?.let {
            addHistory(it, date1)
        }

        val db = writableDatabase
        val values = ContentValues()

        values.put(events, Constant.fullDateFormat(date1))
        values.put(mobileStationLocationDetail, Constant.fullDateFormat(date1))
        values.put(mobileStationCarDetail, Constant.fullDateFormat(date1))
        values.put(dayCloseHistory, Constant.fullDateFormat(date1))
        values.put(date, Constant.fullDateFormat(date1))

        val result = db.insert(mechanicHome, null, values)
        if (db.isOpen) {
            db.close()
        }
        return result != -1L
    }

    private fun addLocationDetail(data: MobileStationLocationDetail, date1: Calendar): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(id, data.id)
        values.put(carId, data.carId)
        values.put(displayId, data.displayId)
        values.put(name, data.name)
        values.put(address, data.address)
        values.put(state, data.state)
        values.put(postalCode, data.postalCode)
        values.put(lat, data.lat)
        values.put(lon, data.lon)
        values.put(customerId, data.customerId)
        values.put(locationType, data.locationType)
        values.put(parentId, data.parentId)
        values.put(officeIcon, data.officeIcon)
        values.put(calendarColor, data.calendarColor)
        values.put(partnerName, data.partnerName)
        values.put(deleted, data.deleted)
        values.put(createdAt, data.createdAt)
        values.put(updatedAt, data.updatedAt)
        values.put(latitude, data.latitude)
        values.put(longitude, data.longitude)
        values.put(mslid, data.mslid)
        values.put(date, Constant.fullDateFormat(date1))

        val result = db.insert(locationDetail, null, values)
        db.close()
        return result != -1L
    }

    private fun addCarDetail(data: MobileStationCarDetail, date1: Calendar): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(id, data.id)
        values.put(numberPlate, data.numberPlate)
        values.put(vehicleVersion, data.vehicleVersion)
        values.put(year, data.year)
        values.put(registrationYear, data.registrationYear)
        values.put(month, data.month)
        values.put(makeId, data.makeId)
        values.put(modelId, data.modelId)
        values.put(bodyType, data.bodyType)
        values.put(totalDoors, data.totalDoors)
        values.put(fuelType, data.fuelType)
        values.put(powerType, data.powerType)
        values.put(transmissionType, data.transmissionType)
        values.put(totalKm, data.totalKm)
        values.put(enterKm, data.enterKm)
        values.put(customerId, data.customerId)
        values.put(locationId, data.locationId)
        values.put(euroClass, data.euroClass)
        values.put(carColor, data.carColor)
        values.put(ownerType, data.ownerType)
        values.put(ownerId, data.ownerId)
        values.put(isMobileStation, data.isMobileStation)
        values.put(carTypology, data.carTypology)
        values.put(carType, data.carType)
        values.put(ledXeon, data.ledXeon)
        values.put(navigationSystem, data.navigationSystem)
        values.put(width, data.width)
        values.put(ratio, data.ratio)
        values.put(diameter, data.diameter)
        values.put(vin, data.vin)
        values.put(dateInstallation, data.dateInstallation)
        values.put(fatt, data.fatt)
        values.put(deleted, data.deleted)
        values.put(createdAt, data.createdAt)
        values.put(updatedAt, data.updatedAt)
        values.put(makeName, data.makeName)
        values.put(businessName, data.businessName)
        values.put(locationName, data.locationName)
        values.put(locationAddress, data.locationAddress)
        values.put(lat, data.lat)
        values.put(lon, data.lon)
        values.put(modelName, data.modelName)
        values.put(date, Constant.fullDateFormat(date1))

        val result = db.insert(carDetail, null, values)
        db.close()
        return result != -1L
    }

    private fun addHistory(data: DayCloseHistory, date1: Calendar): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(id, data.id)
        if (data.date == null) {
            values.put(date, Constant.fullDateFormat(date1))
        } else {
            values.put(date, data.date)
        }
        values.put(mobileStationId, data.mobileStationId)
        values.put(closedBy, data.closedBy)
        values.put(createdAt, data.createdAt)
        values.put(updatedAt, data.updatedAt)

        val result = db.insert(history, null, values)
        db.close()
        return result != -1L
    }

    private fun addEvent(
        data: MechanicHomeEventDetail,
        date1: Calendar,
        context: Context
    ): Boolean {

        if (Constant.getData(context, Constant.SAVE_ID) == null) {
            Constant.saveData(context, Constant.SAVE_ID, 0)
        }

        Constant.saveData(
            context,
            Constant.SAVE_ID,
            (Constant.getData(context, Constant.SAVE_ID) + 1)
        )

        for (temp1 in data.subEvents) {
            addSubEvent(temp1, Constant.getData(context, Constant.SAVE_ID))
        }

        val db = writableDatabase
        val values = ContentValues()

        values.put(id, data.id)
        values.put(displayId, data.displayId)
        values.put(parentWorkPlanId, data.parentWorkPlanId)
        values.put(customerId, data.customerId)
        values.put(mobileStationId, data.mobileStationId)
        values.put(locationId, data.locationId)
        if (data.date == null) {
            values.put(date, Constant.fullDateFormat(date1))
        } else {
            values.put(date, data.date)
        }
        values.put(lat, data.lat)
        values.put(lon, data.lon)
        values.put(address, data.address)
        values.put(isEmailSend, data.isEmailSend)
        values.put(emailSentDate, data.emailSentDate)
        values.put(createdBy, data.createdBy)
        values.put(icon, data.icon)
        values.put(statusId, data.statusId)
        values.put(orderStatusId, data.orderStatusId)
        values.put(inchargeId, data.inchargeId)
        values.put(isOrderNew, data.isOrderNew)
        values.put(isCreatedByMechanic, data.isCreatedByMechanic)
        values.put(isNew, data.isNew)
        values.put(isRead, data.isRead)
        values.put(calendarColor, data.calendarColor)
        values.put(isMessageRead, data.isMessageRead)
        values.put(isReportConfirm, data.isReportConfirm)
        values.put(configuredBy, data.configuredBy)
        values.put(configuredByLocationId, data.configuredByLocationId)
        values.put(note, data.note)
        values.put(deleted, data.deleted)
        values.put(createdAt, data.createdAt)
        values.put(updatedAt, data.updatedAt)
        values.put(customerName, data.customerName)
        values.put(createdByName, data.createdByName)
        values.put(inchargeName, data.inchargeName)
        values.put(statusName, data.statusName)
        values.put(locationName, data.locationName)
        values.put(leadCount, data.leadCount)
        values.put(customerWorkPlanId, data.customerWorkPlanId)
        values.put(isTimeDisplay, data.isTimeDisplay)
        values.put(travelStartTime, data.travelStartTime)
        values.put(workStartTime, data.workStartTime)
        values.put(subEventId, Constant.getData(context, Constant.SAVE_ID))
        values.put(subEvents, data.subEvents.size)

        val result = db.insert(eventsData, null, values)
        db.close()
        return result != -1L
    }

    private fun addSubEvent(data: MechanicHomeSubEvent, tempId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(id, data.id)
        values.put(title, data.title)
        values.put(start, data.start)
        values.put(end, data.end)
        values.put(travelStart, data.travelStart)
        values.put(travelEnd, data.travelEnd)
        values.put(numberPlate, data.numberPlate)
        values.put(backgroundColor, data.backgroundColor)
        values.put(officeCalendarColor, data.officeCalendarColor)
        values.put(leadId, data.leadId)
        values.put(address, data.address)
        values.put(orderId, data.orderId)
        values.put(userId, data.userId)
        values.put(userName, data.userName)
        values.put(description, data.description)
        values.put(actionDate, data.actionDate)
        values.put(title1, data.title1)
        values.put(objectType, data.objectType)
        values.put(contactId, data.contactId)
        values.put(firstName, data.firstName)
        values.put(lastName, data.lastName)
        values.put(email, data.email)
        values.put(allDay, data.allDay)
        values.put(completed, data.completed)
        values.put(mobile, data.mobile)
        values.put(leadTypeId, data.leadTypeId)
        values.put(priority, data.priority)
        values.put(carId, data.carId)
        values.put(mobileStationId, data.mobileStationId)
        values.put(businessName, data.businessName)
        values.put(locationName, data.locationName)
        values.put(currentStep, data.currentStep)
        values.put(isClosed, data.isClosed)
        values.put(isWorkNotDone, data.isWorkNotDone)
        values.put(isDayClose, data.isDayClose)
        values.put(calendarWorkPlanId, data.calendarWorkPlanId)
        values.put(calendarWorkDisplayId, data.calendarWorkDisplayId)
        values.put(workStartTime, data.workStartTime)
        values.put(workEndTime, data.workEndTime)
        values.put(totalKm, data.totalKm)
        values.put(travelTime, data.travelTime)
        values.put(mechanicWorkStartTime, data.mechanicWorkStartTime)
        values.put(mechanicWorkEndTime, data.mechanicWorkEndTime)
        values.put(mechanicTravelStartTime, data.mechanicTravelStartTime)
        values.put(mechanicTravelEndTime, data.mechanicTravelEndTime)
        values.put(isEventStart, data.isEventStart)
        values.put(sourceAddress, data.sourceAddress)
        values.put(destinationAddress, data.destinationAddress)
        values.put(sedeName, data.sedeName)
        values.put(closedAt, data.closedAt)
        values.put(enterKm, data.enterKm)
        values.put(lat, data.lat)
        values.put(lon, data.lon)
        values.put(dropAddress, data.dropAddress)
        values.put(dropLat, data.dropLat)
        values.put(dropLon, data.dropLon)
        values.put(towRequestAccepted, data.towRequestAccepted)
        values.put(isPicked, data.isPicked)
        values.put(isTravelCompleted, data.isTravelCompleted)
        values.put(displayId, data.displayId)
        values.put(mobileStationLat, data.mobileStationLat)
        values.put(mobileStationLon, data.mobileStationLon)
        values.put(isManualReport, data.isManualReport)
        values.put(isPauseTravel, data.isPauseTravel)
        values.put(mechanicTravelBreakStart, data.mechanicTravelBreakStart)
        values.put(mechanicTravelBreakEnd, data.mechanicTravelBreakEnd)
        values.put(mechanicTravelRestart, data.mechanicTravelRestart)
        values.put(customerWorkPlanId, data.customerWorkPlanId)
        values.put(isReportConfirm, data.isReportConfirm)
        values.put(makeName, data.makeName)
        values.put(modelName, data.modelName)
        values.put(note, data.note)
        values.put(isNoteRead, data.isNoteRead)
        values.put(customerWorkPlanStatusId, data.customerWorkPlanStatusId)
        values.put(isOtherVehicleBtn, data.isOtherVehicleBtn)
        values.put(isTimeDisplay, data.isTimeDisplay)
        values.put(isCustomerWorkPlanNoteReadBtn, data.isCustomerWorkPlanNoteReadBtn)
        values.put(isPrintPdfBtn, data.isPrintPdfBtn)
        values.put(workHours, data.workHours)
        values.put(subEventId, tempId)

        val result = db.insert(subEvent, null, values)
        db.close()
        return result != -1L
    }

    fun viewData(date1: Calendar): MechanicHomeJson {

        val ddd = Constant.fullDateFormat(date1)

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $mechanicHome where $date = '$ddd'", null
        )

        val temp = MechanicHomeJson()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                temp.events = getEvent(cursor.getString(0))

                temp.mobileStationLocationDetail = getLocationDetail(cursor.getString(1))

                temp.mobileStationCarDetail = getCarDetail(cursor.getString(2))

                temp.dayCloseHistory = getHistory(cursor.getString(3))
            }
        }

        cursor.close()

        return temp
    }

    private fun getEvent(date1: String): ArrayList<MechanicHomeEvent> {

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $eventsData where $date = '$date1'",
            null
        )

        val data = ArrayList<MechanicHomeEvent>()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                val temp = MechanicHomeEventDetail()

                temp.id = cursor.getInt(0)
                temp.displayId = cursor.getString(1)
                temp.parentWorkPlanId = cursor.getString(2)
                temp.customerId = cursor.getInt(3)
                temp.mobileStationId = cursor.getInt(4)
                temp.locationId = cursor.getInt(5)
                temp.date = cursor.getString(6)
                temp.lat = cursor.getDouble(7)
                temp.lon = cursor.getDouble(8)
                temp.address = cursor.getString(9)
                temp.isEmailSend = cursor.getString(10)
                temp.emailSentDate = cursor.getString(11)
                temp.createdBy = cursor.getInt(12)
                temp.icon = cursor.getString(13)
                temp.statusId = cursor.getInt(14)
                temp.orderStatusId = cursor.getString(15)
                temp.inchargeId = cursor.getString(16)
                temp.isOrderNew = cursor.getString(17)
                temp.isCreatedByMechanic = cursor.getString(18)
                temp.isNew = cursor.getString(19)
                temp.isRead = cursor.getString(20)
                temp.calendarColor = cursor.getString(21)
                temp.isMessageRead = cursor.getString(22)
                temp.isReportConfirm = cursor.getString(23)
                temp.configuredBy = cursor.getString(24)
                temp.configuredByLocationId = cursor.getString(25)
                temp.note = cursor.getString(26)
                temp.deleted = cursor.getString(27)
                temp.createdAt = cursor.getString(28)
                temp.updatedAt = cursor.getString(29)
                temp.customerName = cursor.getString(30)
                temp.createdByName = cursor.getString(31)
                temp.inchargeName = cursor.getString(32)
                temp.statusName = cursor.getString(33)
                temp.locationName = cursor.getString(34)
                temp.leadCount = cursor.getInt(35)
                temp.customerWorkPlanId = cursor.getInt(36)
                temp.isTimeDisplay = cursor.getString(37)
                temp.travelStartTime = cursor.getString(38)
                temp.workStartTime = cursor.getString(39)
                temp.subEvents = getSubEvent(cursor.getInt(41))
                temp.subEventId = cursor.getInt(41)

                val dataTemp = MechanicHomeEvent()
                dataTemp.detail = temp
                data.add(dataTemp)
            }
        }
        cursor.close()

        return data
    }

    private fun getSubEvent(idTemp: Int): ArrayList<MechanicHomeSubEvent> {
        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $subEvent where $subEventId = '$idTemp'",
            null
        )

        val data = ArrayList<MechanicHomeSubEvent>()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                val temp = MechanicHomeSubEvent()

                temp.id = cursor.getInt(0)
                temp.title = cursor.getString(1)
                temp.start = cursor.getString(2)
                temp.end = cursor.getString(3)
                temp.travelStart = cursor.getString(4)
                temp.travelEnd = cursor.getString(5)
                temp.numberPlate = cursor.getString(6)
                temp.backgroundColor = cursor.getString(7)
                temp.officeCalendarColor = cursor.getString(8)
                temp.leadId = cursor.getInt(9)
                temp.address = cursor.getString(10)
                temp.orderId = cursor.getInt(11)
                temp.userId = cursor.getInt(12)
                temp.userName = cursor.getString(13)
                temp.description = cursor.getString(14)
                temp.actionDate = cursor.getString(15)
                temp.title1 = cursor.getString(16)
                temp.objectType = cursor.getString(17)
                temp.contactId = cursor.getInt(18)
                temp.firstName = cursor.getString(19)
                temp.lastName = cursor.getString(20)
                temp.email = cursor.getString(21)
                if (cursor.getString(22) != null) {
                    temp.allDay = cursor.getString(22).toBoolean()
                }
                temp.completed = cursor.getString(23)
                temp.mobile = cursor.getString(24)
                temp.leadTypeId = cursor.getInt(25)
                temp.priority = cursor.getInt(26)
                temp.carId = cursor.getInt(27)
                temp.mobileStationId = cursor.getInt(28)
                temp.businessName = cursor.getString(29)
                temp.locationName = cursor.getString(30)
                temp.currentStep = cursor.getString(31)
                temp.isClosed = cursor.getString(32)
                temp.isWorkNotDone = cursor.getString(33)
                temp.isDayClose = cursor.getString(34)
                temp.calendarWorkPlanId = cursor.getInt(35)
                temp.calendarWorkDisplayId = cursor.getString(36)
                temp.workStartTime = cursor.getString(37)
                temp.workEndTime = cursor.getString(38)
                temp.totalKm = cursor.getFloat(39)
                temp.travelTime = cursor.getDouble(40)
                temp.mechanicWorkStartTime = cursor.getString(41)
                temp.mechanicWorkEndTime = cursor.getString(42)
                temp.mechanicTravelStartTime = cursor.getString(43)
                temp.mechanicTravelEndTime = cursor.getString(44)
                temp.isEventStart = cursor.getString(45)
                temp.sourceAddress = cursor.getString(46)
                temp.destinationAddress = cursor.getString(47)
                temp.sedeName = cursor.getString(48)
                temp.closedAt = cursor.getString(49)
                temp.enterKm = cursor.getInt(50)
                temp.lat = cursor.getDouble(51)
                temp.lon = cursor.getDouble(52)
                temp.dropAddress = cursor.getString(53)
                temp.dropLat = cursor.getString(54)
                temp.dropLon = cursor.getString(55)
                temp.towRequestAccepted = cursor.getString(56)
                temp.isPicked = cursor.getString(57)
                temp.isTravelCompleted = cursor.getString(58)
                temp.displayId = cursor.getString(59)
                temp.mobileStationLat = cursor.getDouble(60)
                temp.mobileStationLon = cursor.getDouble(61)
                temp.isManualReport = cursor.getString(62)
                temp.isPauseTravel = cursor.getString(63)
                temp.mechanicTravelBreakStart = cursor.getString(64)
                temp.mechanicTravelBreakEnd = cursor.getString(65)
                temp.mechanicTravelRestart = cursor.getString(66)
                temp.customerWorkPlanId = cursor.getInt(67)
                temp.isReportConfirm = cursor.getString(68)
                temp.makeName = cursor.getString(69)
                temp.modelName = cursor.getString(70)
                temp.note = cursor.getString(71)
                temp.isNoteRead = cursor.getString(72)
                temp.customerWorkPlanStatusId = cursor.getString(73)
                temp.isOtherVehicleBtn = cursor.getString(74)
                temp.isTimeDisplay = cursor.getString(75)
                temp.isCustomerWorkPlanNoteReadBtn = cursor.getString(76)
                temp.isPrintPdfBtn = cursor.getString(77)
                temp.workHours = cursor.getDouble(78)

                data.add(temp)
            }
        }
        cursor.close()

        return data
    }

    private fun getLocationDetail(date1: String): MobileStationLocationDetail {

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $locationDetail where $date = '$date1'",
            null
        )

        val temp = MobileStationLocationDetail()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                temp.id = cursor.getInt(0)
                temp.carId = cursor.getInt(1)
                temp.displayId = cursor.getString(2)
                temp.name = cursor.getString(3)
                temp.address = cursor.getString(4)
                temp.state = cursor.getString(5)
                temp.postalCode = cursor.getString(6)
                temp.lat = cursor.getDouble(7)
                temp.lon = cursor.getDouble(8)
                temp.customerId = cursor.getString(9)
                temp.locationType = cursor.getString(10)
                temp.parentId = cursor.getInt(11)
                temp.officeIcon = cursor.getString(12)
                temp.calendarColor = cursor.getString(13)
                temp.partnerName = cursor.getString(14)
                temp.deleted = cursor.getString(15)
                temp.createdAt = cursor.getString(16)
                temp.updatedAt = cursor.getString(17)
                temp.latitude = cursor.getDouble(18)
                temp.longitude = cursor.getDouble(19)
                temp.mslid = cursor.getInt(20)

            }
        }
        cursor.close()

        return temp
    }

    fun getCarDetail(date1: String): MobileStationCarDetail {

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $carDetail where $date = '$date1'",
            null
        )

        val temp = MobileStationCarDetail()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                temp.id = cursor.getInt(0)
                temp.numberPlate = cursor.getString(1)
                temp.vehicleVersion = cursor.getString(2)
                temp.year = cursor.getInt(3)
                temp.registrationYear = cursor.getString(4)
                temp.month = cursor.getString(5)
                temp.makeId = cursor.getInt(6)
                temp.modelId = cursor.getString(7)
                temp.bodyType = cursor.getString(8)
                temp.totalDoors = cursor.getString(9)
                temp.fuelType = cursor.getString(10)
                temp.powerType = cursor.getString(11)
                temp.transmissionType = cursor.getString(12)
                temp.totalKm = cursor.getFloat(13)
                temp.enterKm = cursor.getString(14)
                temp.customerId = cursor.getString(15)
                temp.locationId = cursor.getString(16)
                temp.euroClass = cursor.getInt(17)
                temp.carColor = cursor.getString(18)
                temp.ownerType = cursor.getString(19)
                temp.ownerId = cursor.getString(20)
                temp.isMobileStation = cursor.getString(21)
                temp.carTypology = cursor.getString(22)
                temp.carType = cursor.getString(23)
                temp.ledXeon = cursor.getString(24)
                temp.navigationSystem = cursor.getString(25)
                temp.width = cursor.getString(26)
                temp.ratio = cursor.getString(27)
                temp.diameter = cursor.getString(28)
                temp.vin = cursor.getString(29)
                temp.dateInstallation = cursor.getString(30)
                temp.fatt = cursor.getString(31)
                temp.deleted = cursor.getString(32)
                temp.createdAt = cursor.getString(33)
                temp.updatedAt = cursor.getString(34)
                temp.makeName = cursor.getString(35)
                temp.businessName = cursor.getString(36)
                temp.locationName = cursor.getString(37)
                temp.locationAddress = cursor.getString(38)
                temp.lat = cursor.getString(39)
                temp.lon = cursor.getString(40)
                temp.modelName = cursor.getString(41)

            }
        }
        cursor.close()

        return temp
    }

    private fun getHistory(date1: String): DayCloseHistory {

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $history where $date = '$date1'",
            null
        )

        val temp = DayCloseHistory()

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                temp.id = cursor.getInt(0)
                temp.date = cursor.getString(1)
                temp.mobileStationId = cursor.getInt(2)
                temp.closedBy = cursor.getInt(3)
                temp.createdAt = cursor.getString(4)
                temp.updatedAt = cursor.getString(5)

            }
        }
        cursor.close()

        return temp
    }

    fun deleteData(date1: Calendar): Boolean {

        val ddd = Constant.fullDateFormat(date1)

        val cursor: Cursor = writableDatabase.rawQuery(
            "select * from $mechanicHome where $date = '$ddd'", null
        )

        var cur0 = ""
        var cur1 = ""
        var cur2 = ""
        var cur3 = ""

        if (cursor.count != 0) {
            while (cursor.moveToNext()) {

                cur0 = cursor.getString(0)
                cur1 = cursor.getString(1)
                cur2 = cursor.getString(2)
                cur3 =
                    if (cursor.getString(3) != null) {
                        cursor.getString(3)
                    } else {
                        ""
                    }
            }
        }

        cursor.close()

        if (cur1 != "") {
            val db2 = writableDatabase
            db2.delete(
                locationDetail,
                "$date= ? \n", arrayOf(cur1)
            )
            db2.close()
        }

        if (cur2 != "") {
            val db3 = writableDatabase
            db3.delete(
                carDetail,
                "$date= ? \n", arrayOf(cur2)
            )
            db3.close()
        }

        if (cur3 != "") {
            val db4 = writableDatabase
            db4.delete(
                history,
                "$date= ? \n", arrayOf(cur3)
            )
            db4.close()
        }

        val cursor2: Cursor = writableDatabase.rawQuery(
            "select * from $eventsData where $date = '$ddd'",
            null
        )

        if (cursor2.count != 0) {
            while (cursor.moveToNext()) {
                val db6 = writableDatabase
                db6.delete(
                    subEvent,
                    "$subEventId= ? \n", arrayOf(cursor.getString(41))
                )
                db6.close()
            }
        }

        cursor2.close()

        if (cur0 != "") {
            val db5 = writableDatabase
            db5.delete(
                eventsData,
                "$date= ? \n", arrayOf(cur0)
            )
            db5.close()
        }


        val db1 = writableDatabase
        val isDeleted = db1.delete(
            mechanicHome,
            "$date= ? \n", arrayOf(Constant.fullDateFormat(date1))
        )
        db1.close()
        return isDeleted != 0
    }
}