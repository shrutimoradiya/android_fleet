package com.antares.fleetservice.view.fragment

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.antares.fleetservice.R
import com.antares.fleetservice.app.ConnectivityReceiver
import com.antares.fleetservice.app.ServiceApp
import com.antares.fleetservice.dataBase.*
import com.antares.fleetservice.databinding.FragmentCalendarBinding
import com.antares.fleetservice.model.login.LoginUser
import com.antares.fleetservice.model.mechanic.home.MechanicHomeEvent
import com.antares.fleetservice.model.mechanic.home.MechanicHomeJson
import com.antares.fleetservice.model.mechanic.home.MechanicHomeSubEvent
import com.antares.fleetservice.model.users.Users
import com.antares.fleetservice.utils.Constant
import com.antares.fleetservice.utils.SharedPref
import com.antares.fleetservice.view.activity.*
import com.antares.fleetservice.view.adapter.CalendarEventAdapter
import com.antares.fleetservice.view.adapter.CalendarSubEventAdapter
import com.antares.fleetservice.view.widget.dialog.AlertDialog
import com.antares.fleetservice.view.widget.dialog.ConfirmDialog
import com.antares.fleetservice.view.widget.dialog.DatePickerDialog
import com.antares.fleetservice.view.widget.dialog.InfoDialog
import com.antares.fleetservice.view.widget.dialog.InfoDialogWithButton
import com.antares.fleetservice.viewmodel.HomeCalendarViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*


open class FragmentCalendar : Fragment(),
    CalendarSubEventAdapter.OnSubEventClickListener, DatePickerDialog.Callback,
    ConnectivityReceiver.ConnectivityReceiverListener {

    private var _binding: FragmentCalendarBinding? = null
    private var adapter: CalendarEventAdapter? = null
    private var contactHomeViewModel: HomeCalendarViewModel? = null
    private var arrayList = ArrayList<MechanicHomeEvent>()
    private var datePicker: DatePickerDialog? = null
    private var loginId: Int = 0
    private var mobileStationId: Int = 0
    private var isClickLock: Boolean = false
    private var myDbh: ActionDateDB? = null
    private var myDbh2: MechanicHomeDB? = null
    private var isFirst: Boolean = true
    private var isTravelChange: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        contactHomeViewModel = activity?.application?.let {
            ViewModelProvider.AndroidViewModelFactory
                .getInstance(it)
                .create(HomeCalendarViewModel::class.java)
        }

        if (Constant.isOnline(requireContext())) {
            val myDbhContact = ContactUsDB(requireContext())

            val arr: ArrayList<Users> = myDbhContact.viewData()
            if (arr.size == 0) {
                contactHomeViewModel?.getContactList(requireActivity() as AppCompatActivity)
            }
            if (!Constant.getDataStr(requireContext(), Constant.SAVE_DATE)
                    .equals(Constant.fullDateFormat(contactHomeViewModel?.cal!!))
            ) {
                contactHomeViewModel?.getPresetList(requireActivity() as AppCompatActivity)
                Constant.saveDataStr(
                    requireContext(),
                    Constant.SAVE_DATE,
                    Constant.fullDateFormat(contactHomeViewModel?.cal!!)
                )
            }
        }

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context?.registerReceiver(ConnectivityReceiver(), intentFilter)

        context?.let { it ->
            SharedPref(it).userInfo?.let {
                try {
                    Gson().fromJson(it, LoginUser::class.java)?.let { it1 ->
                        mobileStationId = it1.mobileStationId ?: mobileStationId
                        loginId = it1.id ?: loginId
                    }
                } catch (exp: Exception) {
                }
            }
        }

        myDbh = ActionDateDB(requireContext())
        myDbh2 = MechanicHomeDB(requireContext())

        val myFabSrc = resources.getDrawable(R.drawable.icon_unlock)

        val willBeWhite = myFabSrc.constantState?.newDrawable()

        willBeWhite?.mutate()?.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        _binding?.iconLock?.apply {
            setImageDrawable(willBeWhite)
            setOnClickListener {
                if (isClickLock) {
                    val intent = Intent(
                        this@FragmentCalendar.requireContext(),
                        CalendarLockDetailActivity::class.java
                    )
                    val mBundle = Bundle()
                    mBundle.putSerializable(
                        Constant.DATE_KEY,
                        contactHomeViewModel?.cal
                    )
                    intent.putExtras(mBundle)
                    startActivity(intent)
                }
            }
        }

        _binding?.let {

            it.header.viewHeader.visibility = View.GONE

            it.pullToRefresh.setOnRefreshListener {
                setEventData()
                _binding?.pullToRefresh?.isRefreshing = false
            }

            it.header.viewMonth.setOnClickListener {
                datePicker?.onDaySelected((activity as MainActivity).cal)
                datePicker?.show()
            }

            it.header.viewDate.setOnClickListener {
                datePicker?.onDaySelected((activity as MainActivity).cal)
                datePicker?.show()
            }

            it.header.truck.setOnClickListener {
                val intent = Intent(
                    this@FragmentCalendar.requireContext(),
                    CalendarDeliveryDetailActivity::class.java
                )
                val mBundle = Bundle()
                mBundle.putSerializable(Constant.DATE_KEY, contactHomeViewModel?.cal)
                intent.putExtras(mBundle)
                startActivity(intent)
            }

            this@FragmentCalendar.activity?.let { activity ->
                adapter =
                    CalendarEventAdapter(activity, arrayList, contactHomeViewModel?.cal, this)
                it.recyclerView.adapter = adapter
                it.recyclerView.isNestedScrollingEnabled = false
            }
        }
        return _binding?.root
    }

    private fun setProgressBar() {
        _binding?.let {
            it.noDataFound.noDataFound.visibility = View.GONE
            it.recyclerView.visibility = View.VISIBLE
            it.progressbar.visibility = View.VISIBLE
        }
    }

    private fun setEventData() {
        if (Constant.isOnline(requireContext())) {
            contactHomeViewModel?.let { viewModel ->
                viewModel.cal.time = (activity as MainActivity).cal.time
                setProgressBar()

                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                map["created_from"] = Constant.createFrom
                map["ids"] = loginId
                map["mobile_station_id"] = mobileStationId

                viewModel.getCalenderEvent(map)
                viewModel.calendarEventData.observe(viewLifecycleOwner) { data ->

                    val dateList = ArrayList<String>()

                    val dateListTemp: ArrayList<String> = myDbh?.viewData()!!

                    if (data.isNotEmpty()) {
                        if (dateListTemp.size != 0) {

                            if (myDbh?.deleteData() == true) {

                                for (event in data) {
                                    event.actionDate?.split(" ")?.let {
                                        val dateFinal: String

                                        if (it.isNotEmpty()) {
                                            val finalDate = it[0]
                                            val dateDisplay = finalDate.split("-")
                                            dateFinal =
                                                "${dateDisplay[0]}-${dateDisplay[1]}-${dateDisplay[2]}"
                                            dateList.add(dateFinal)
                                            myDbh?.addData(dateFinal)
                                        }
                                    }
                                }
                            }
                        } else {
                            for (event in data) {
                                event.actionDate?.split(" ")?.let {
                                    val dateFinal: String

                                    if (it.isNotEmpty()) {
                                        val finalDate = it[0]
                                        val dateDisplay = finalDate.split("-")
                                        dateFinal =
                                            "${dateDisplay[0]}-${dateDisplay[1]}-${dateDisplay[2]}"
                                        dateList.add(dateFinal)
                                        myDbh?.addData(dateFinal)
                                    }
                                }
                            }
                        }
                    }

                    if (this.activity is MainActivity) {
                        datePicker =
                            DatePickerDialog(this.activity as AppCompatActivity, dateList, this)
                    }
                    setDate()
                    _binding?.header?.viewHeader?.visibility = View.VISIBLE
                }

                viewModel.homeData.observe(viewLifecycleOwner) { data ->


                    val dataTemp: MechanicHomeJson =
                        myDbh2?.viewData((activity as MainActivity).cal)!!

                    if (dataTemp.events.size != 0 || data.events.size != 0) {
                        if (myDbh2?.deleteData((activity as MainActivity).cal) == true || dataTemp.events.size == 0) {
                            myDbh2?.addData(
                                data,
                                (activity as MainActivity).cal,
                                activity as AppCompatActivity
                            )
                        }
                    }

                    arrayList.clear()
                    for (temp in data.events) {
                        temp.isDayClose = data.dayCloseHistory != null
                    }
                    arrayList.addAll(data.events)


                    _binding?.progressbar?.visibility = View.GONE

                    val arrId = ArrayList<Int>()

                    for (temp in data.events) {
                        for (tem in temp.detail!!.subEvents) {
                            arrId.add(tem.id!!)
                        }
                    }

                    viewModel.saveSubEventData(
                        arrId,
                        requireContext() as AppCompatActivity,
                        data.version,
                        data.apk_url,
                        Constant.dateForApi1((activity as MainActivity).cal)
                    )

                    val arrMapId = ArrayList<Int>()

                    for (temp in data.events) {
                        for (tem in temp.detail!!.subEvents) {
                            if (!tem.travelStart.isNullOrEmpty() && !tem.travelEnd.isNullOrEmpty()) {
                                arrMapId.add(tem.id!!)
                            }
                        }
                    }

                    viewModel.saveMapData(
                        requireContext() as AppCompatActivity,
                        arrMapId,
                        Constant.fullDateFormat(contactHomeViewModel?.cal!!),
                        mobileStationId,
                        isTravelChange
                    )

                    isTravelChange = false

                    _binding?.iconLock?.background?.setTint(
                        ContextCompat.getColor(
                            requireContext(), R.color.gray
                        )
                    )

                    if (arrayList.size == 0) {
                        _binding?.noDataFound?.noDataFound?.visibility = View.VISIBLE
                    } else {
                        _binding?.recyclerView?.visibility = View.VISIBLE
                        isClickLock = if (data.dayCloseHistory == null) {
                            _binding?.iconLock?.background?.setTint(
                                ContextCompat.getColor(
                                    requireContext(), R.color.gray
                                )
                            )
                            true
                        } else {
                            if (data.dayCloseHistory!!.id == null) {
                                _binding?.iconLock?.background?.setTint(
                                    ContextCompat.getColor(
                                        requireContext(), R.color.gray
                                    )
                                )
                                true
                            } else {
                                _binding?.iconLock?.background?.setTint(
                                    ContextCompat.getColor(
                                        requireContext(), R.color.green
                                    )
                                )
                                false
                            }
                        }
                    }
                    adapter?.notifyDataSetChanged()
                    _binding?.header?.modelNo?.text = data.mobileStationCarDetail?.numberPlate
                }
            }
        } else {
            contactHomeViewModel?.let { viewModel ->
                viewModel.cal.time = (activity as MainActivity).cal.time

                setProgressBar()

                if (isFirst) {
                    val dateList: ArrayList<String> = myDbh?.viewData()!!

                    if (this.activity is MainActivity) {
                        datePicker =
                            DatePickerDialog(this.activity as AppCompatActivity, dateList, this)
                    }

                    isFirst = false
                    setDate()
                    _binding?.header?.viewHeader?.visibility = View.VISIBLE
                }

                val data: MechanicHomeJson = myDbh2?.viewData((activity as MainActivity).cal)!!

                arrayList.clear()
                for (temp in data.events) {
                    temp.isDayClose = data.dayCloseHistory != null
                }
                arrayList.addAll(data.events)
                _binding?.progressbar?.visibility = View.GONE

                _binding?.iconLock?.background?.setTint(
                    ContextCompat.getColor(
                        requireContext(), R.color.gray
                    )
                )

                if (arrayList.size == 0) {
                    _binding?.noDataFound?.noDataFound?.visibility = View.VISIBLE
                } else {
                    _binding?.recyclerView?.visibility = View.VISIBLE
                    isClickLock = if (data.dayCloseHistory == null) {
                        _binding?.iconLock?.background?.setTint(
                            ContextCompat.getColor(
                                requireContext(), R.color.gray
                            )
                        )
                        true
                    } else {
                        if (data.dayCloseHistory!!.id == null || data.dayCloseHistory!!.id == 0) {
                            _binding?.iconLock?.background?.setTint(
                                ContextCompat.getColor(
                                    requireContext(), R.color.gray
                                )
                            )
                            true
                        } else {
                            _binding?.iconLock?.background?.setTint(
                                ContextCompat.getColor(
                                    requireContext(), R.color.green
                                )
                            )
                            false
                        }
                    }
                }
                adapter?.notifyDataSetChanged()
                _binding?.header?.modelNo?.text = data.mobileStationCarDetail?.numberPlate
            }
        }

    }

    private fun setDate() {
        contactHomeViewModel?.let { viewModel ->
            _binding?.let {
                it.header.days.text =
                    SimpleDateFormat(Constant.DAYS_FORMAT).format(viewModel.cal.time)
                it.header.dates.text =
                    SimpleDateFormat(Constant.DD_FORMAT).format(viewModel.cal.time)
                it.header.month.text =
                    SimpleDateFormat(Constant.MONTH_FORMAT).format(viewModel.cal.time)
                it.header.year.text =
                    SimpleDateFormat(Constant.YEAR_FORMAT).format(viewModel.cal.time)
            }
            arrayList.clear()
            adapter?.notifyDataSetChanged()
            setProgressBar()
            if (Constant.isOnline(requireContext())) {
                viewModel.getCalenderHomeDetail(loginId, mobileStationId)
            } else {
                setEventData()
            }
        }
    }

    override fun onClick(model: MechanicHomeSubEvent?) {
        model?.let { it ->
            it.id?.let {
                val intent = Intent(context, AttendanceActivity::class.java)
                val mBundle = Bundle()
                mBundle.putInt(Constant.EVENT_ID_KEY, it)
                mBundle.putSerializable(
                    Constant.DATE_KEY,
                    contactHomeViewModel?.cal ?: Calendar.getInstance()
                )
                intent.putExtras(mBundle)
                context?.startActivity(intent)
            }
        }
    }

    override fun onInfoClick(model: MechanicHomeSubEvent?) {
        model?.note?.let {
            if (it.isNotEmpty()) {

                val subTitle: String =
                    if (model.locationName != null) {
                        "${model.businessName} - ${model.locationName}"
                    } else {
                        "${model.businessName} - ${model.sedeName}"
                    }

                if (this.activity is MainActivity) {
                    InfoDialogWithButton(
                        this.activity as AppCompatActivity,
                        it,
                        subTitle,
                        object : InfoDialogWithButton.OnLettoClick {
                            override fun onItemClick() {
                                model.customerWorkPlanId?.let { it1 ->

                                    val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                                    map["created_from"] = Constant.createFrom
                                    map["type"] = "customer-work-plan-note-read"
                                    map["customer_work_plan_id"] = it1

                                    contactHomeViewModel?.lettoClick(map)
                                }
                            }
                        }
                    ).show()
                }
            }
        }
    }

    override fun onTravelEventlick(model: MechanicHomeSubEvent?) {
        contactHomeViewModel?.let { viewModel ->
            model?.let {
                it.id?.let { id ->
                    it.leadId?.let { leadId ->
                        arrayList.clear()
                        adapter?.notifyDataSetChanged()
                        if (Constant.isOnline(requireContext())) {
                            isTravelChange = true
                            if (it.isEventStart == "Y") {

                                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                                map["created_from"] = Constant.createFrom
                                map["type"] = "travel-end"
                                map["lead_id"] = leadId
                                map["event_id"] = id
                                map["mobile_station_id"] = mobileStationId

                                viewModel.getTravelStartEnd(map)

                            } else {

                                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                                map["created_from"] = Constant.createFrom
                                map["type"] = "travel-start"
                                map["lead_id"] = leadId
                                map["event_id"] = id
                                map["mobile_station_id"] = mobileStationId

                                viewModel.getTravelStartEnd(map)
                            }
                            viewModel.eventCompleted.observeForever {
                                viewModel.getCalenderHomeDetail(loginId, mobileStationId)
                            }
                        } else {
                            if (it.isEventStart == "Y") {
                                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                                map["created_from"] = Constant.createFrom
                                map["type"] = "travel-end"
                                map["lead_id"] = leadId
                                map["event_id"] = id
                                map["mobile_station_id"] = mobileStationId
                                map["date_time"] = Constant.dateForApi()

                                val myDbhTemp = ApiCallLeftDB(requireContext())
                                myDbhTemp.addData(
                                    requireContext(),
                                    Constant.map,
                                    Gson().toJson(map)
                                )

                                val myDbhMap = MapEventDB(requireContext())
                                val dataMaster = myDbhMap.viewData(id)
                                dataMaster.travelEvent?.isEventStart = "N"
                                dataMaster.travelEvent?.isTravelCompleted = "Y"
                                dataMaster.travelEvent?.mechanicTravelEndTime =
                                    Constant.timeForShow()

                                myDbhMap.deleteData(id)
                                myDbhMap.addData(dataMaster)

                                val dataMaster2 = myDbh2?.viewData((activity as MainActivity).cal)
                                for (temp in dataMaster2?.events!!) {
                                    for (tem in temp.detail?.subEvents!!) {
                                        if (tem.id == id) {
                                            tem.isEventStart = "N"
                                            tem.isTravelCompleted = "Y"
                                            tem.mechanicTravelEndTime = Constant.timeForShow()
                                        }
                                    }
                                }

                                myDbh2?.deleteData((activity as MainActivity).cal)
                                myDbh2?.addData(
                                    dataMaster2,
                                    (activity as MainActivity).cal,
                                    requireContext()
                                )

                                setEventData()

                            } else {

                                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                                map["created_from"] = Constant.createFrom
                                map["type"] = "travel-start"
                                map["lead_id"] = leadId
                                map["event_id"] = id
                                map["mobile_station_id"] = mobileStationId
                                map["date_time"] = Constant.dateForApi()

                                val myDbhTemp = ApiCallLeftDB(requireContext())
                                myDbhTemp.addData(
                                    requireContext(),
                                    Constant.map,
                                    Gson().toJson(map)
                                )

                                val myDbhMap = MapEventDB(requireContext())
                                val dataMaster = myDbhMap.viewData(id)
                                dataMaster.travelEvent?.isEventStart = "Y"
                                dataMaster.travelEvent?.mechanicTravelStartTime =
                                    Constant.timeForShow()

                                myDbhMap.deleteData(id)
                                myDbhMap.addData(dataMaster)

                                val dataMaster2 = myDbh2?.viewData((activity as MainActivity).cal)
                                for (temp in dataMaster2?.events!!) {
                                    for (tem in temp.detail?.subEvents!!) {
                                        if (tem.id == id) {
                                            tem.isEventStart = "Y"
                                            tem.mechanicTravelStartTime = Constant.timeForShow()
                                        }
                                    }
                                }

                                myDbh2?.deleteData((activity as MainActivity).cal)
                                myDbh2?.addData(
                                    dataMaster2,
                                    (activity as MainActivity).cal,
                                    requireContext()
                                )

                                setEventData()

                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPDFClick(work_plan_id: Int, date: Calendar) {
        contactHomeViewModel?.let { viewModel ->
            viewModel.cal.time = (activity as MainActivity).cal.time
            setProgressBar()

            val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
            map["created_from"] = Constant.createFrom
            map["customer_work_plan_id"] = work_plan_id
            map["date"] = Constant.fullDateFormat(date)

            viewModel.getPDF(map)
            viewModel.pdfLink.observe(viewLifecycleOwner) { data ->
                _binding?.progressbar?.visibility = View.GONE
                if (data != null) {
                    val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.pdfUrl))
                    startActivity(myIntent)
                }
            }
        }
    }

    override fun onPDFDownloadClick(work_plan_id: Int) {
        contactHomeViewModel?.let { viewModel ->
            val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
            map["created_from"] = Constant.createFrom
            map["type"] = "customer_work_plan"
            map["id"] = work_plan_id
            map["pdf"] = true

            viewModel.getPDFDownload(activity as AppCompatActivity,map)
            viewModel.pdfDownloadLink.observe(viewLifecycleOwner) { data ->
                if (data.code == 200) {
                    if (data != null) {
                        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.pdfUrl))
                        startActivity(myIntent)
                    }
                } else if (data.code == 300) {
                    AlertDialog(
                        requireContext(),
                        getString(R.string.oops),
                        getString(R.string.alert_car_spinner),
                        true,
                        object : AlertDialog.OnOptionSelected {
                            override fun onConfirmClick() {}
                            override fun onAggiungiMezzoClick() {
                            }
                        }
                    ).show()
                }

                _binding?.progressbar?.visibility = View.GONE

            }
        }
    }

    override fun onSubEventClick(subEvent: MechanicHomeSubEvent?, isBtnVisible: Boolean) {
        val eventId = subEvent?.id ?: 0
        val carId = subEvent?.carId ?: 0
        contactHomeViewModel?.let { viewModel ->
            if (this.activity is MainActivity && eventId > 0) {
                (this.activity as MainActivity).setFragment(
                    FragmentModelDetail(
                        isBtnVisible = isBtnVisible,
                        event_id = eventId,
                        date = viewModel.cal,
                        fromScreen = Constant.FROM_CAL_KEY,
                        carId = carId
                    )
                )
            }
        }
    }

    override fun onSubInfoClick(subEvent: MechanicHomeSubEvent?) {
        subEvent?.note?.let {
            if (it.isNotEmpty()) {
                if (this.activity is MainActivity) {
                    InfoDialog(this.activity as AppCompatActivity, it).show()
                }
            }
        }
    }

    override fun onSubEventDeleteClick(subEvent: MechanicHomeSubEvent?) {
        if (Constant.isOnline(requireContext())) {
            ConfirmDialog(
                activity as AppCompatActivity,
                getString(R.string.delete_confirm),
                object : ConfirmDialog.OnOptionSelected {
                    override fun onConfirmClick() {
                        subEvent?.leadId?.let {

                            val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()
                            map["created_from"] = Constant.createFrom
                            map["delete_id"] = it
                            map["delete_type"] = "lead"

                            contactHomeViewModel?.deleteSubEvent(
                                activity as AppCompatActivity,
                                login_id = loginId,
                                mobile_station_id = mobileStationId,
                                map
                            )
                        }
                    }

                    override fun onNotConfirmClick() {}
                }
            ).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.offline), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDateSelected(selectedDate: Calendar?) {
        isFirst = true
        contactHomeViewModel?.let { viewModel ->
            viewModel.cal.time = selectedDate?.time!!
            (activity as MainActivity).cal.time = viewModel.cal.time
        }
        setDate()
    }

    override fun onResume() {
        super.onResume()
        setEventData()
        _binding?.progressbar?.visibility = View.GONE
        ServiceApp.mInstance?.setConnectivityListener(this)
    }

    override fun onPause() {
        super.onPause()
        ServiceApp.mInstance?.setConnectivityListener(null)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        ServiceApp.mInstance?.setNetwork(isConnected, context as AppCompatActivity)
    }
}