package com.antares.fleetservice.view.activity

import android.content.IntentFilter
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.antares.fleetservice.R
import com.antares.fleetservice.app.ConnectivityReceiver
import com.antares.fleetservice.app.ServiceApp
import com.antares.fleetservice.databinding.ActivityCalendarDelieveryDetailBinding
import com.antares.fleetservice.model.calendarDeliveryDetail.CalendarDeliveryDetailEvents
import com.antares.fleetservice.model.calendarDeliveryDetail.CalendarDeliveryDetailLeadItems
import com.antares.fleetservice.model.login.LoginUser
import com.antares.fleetservice.utils.Constant
import com.antares.fleetservice.utils.SharedPref
import com.antares.fleetservice.view.adapter.CalendarDeliverModelAdapter
import com.antares.fleetservice.viewmodel.CalendarDeliveryDetailViewModel
import com.google.gson.Gson
import java.util.*

class CalendarDeliveryDetailActivity : AppCompatActivity(),
    CalendarDeliverModelAdapter.onClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private var arrayList: ArrayList<CalendarDeliveryDetailEvents> = ArrayList()
    private val binding: ActivityCalendarDelieveryDetailBinding by lazy {
        ActivityCalendarDelieveryDetailBinding.inflate(
            layoutInflater
        )
    }

    private var userId: Int = 0
    var date: Calendar? = null
    private var mobileStationId: Int = 0

    private val calendarDeliveryDetailViewModel: CalendarDeliveryDetailViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
            .create(CalendarDeliveryDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        this.registerReceiver(ConnectivityReceiver(), intentFilter)

        binding.iconBack.setOnClickListener {
            onBackPressed()
        }

        intent?.extras?.let {
            date = it.getSerializable(Constant.DATE_KEY) as Calendar?
        }

        setProgressBar()
        binding.modelRecyclerView.adapter = CalendarDeliverModelAdapter(this, arrayList, this)

        SharedPref(this).userInfo?.let {
            try {
                Gson().fromJson(it, LoginUser::class.java)?.let { it1 ->
                    userId = it1.id ?: userId
                    mobileStationId = it1.mobileStationId ?: mobileStationId
                }
            } catch (exp: Exception) {

            }
        }

        calendarDeliveryDetailViewModel.let {
            it.getCalendarDeliveryDetail(
                id = userId,
                date = Constant.fullDateFormat(date!!)
            )

            it.lockData.observeForever { list ->
                arrayList.clear()
                arrayList.addAll(list)
                binding.modelRecyclerView.adapter?.notifyDataSetChanged()

                binding.progressbar.visibility = View.GONE

                if (arrayList.size == 0) {
                    binding.noDataFound.noDataFound.visibility = View.VISIBLE
                } else {
                    binding.nestedScrollView.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val list = arrayList.let { dt ->
                dt.filter { dt1 ->
                    dt1.isSelected
                }
            }

            if (list.size == arrayList.size) {
                val map: ArrayMap<String?, Any?> = ArrayMap<String?, Any?>()

                map["created_from"] = Constant.createFrom
                map["mobile_station_id"] = mobileStationId

                for (temp in list) {
                    val subList = temp.leadItems.let { dt ->
                        dt.filter { dt1 ->
                            dt1.isSelected
                        }
                    }

                    for (temp2 in subList) {
                        map["is_caricato[${temp.leadId}][${temp2.id}]"] = "Y"
                    }
                }

                Log.d("onCreate: ", map.toString())
                setProgressBar()
                calendarDeliveryDetailViewModel.postCalendarDeliveryDetail(
                    map = map, id = userId,
                    date = Constant.fullDateFormat(date!!)
                )
            } else {
                Toast.makeText(
                    ServiceApp.context,
                    getString(R.string.select_all),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setProgressBar() {
        binding.let {
            it.noDataFound.noDataFound.visibility = View.GONE
            it.nestedScrollView.visibility = View.GONE
            it.progressbar.visibility = View.VISIBLE
        }
    }

    override fun onCalenderLockItemClick(subModel: CalendarDeliveryDetailEvents?) {

    }

    override fun onCalenderLockTextModelClick(
        subModel: CalendarDeliveryDetailLeadItems?,
        lockEvent: CalendarDeliveryDetailEvents?
    ) {
        lockEvent?.let {
            it.isSelected = true
            subModel?.isSelected = !(subModel?.isSelected ?: false)
            binding.modelRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        ServiceApp.mInstance?.setConnectivityListener(this)
    }

    override fun onPause() {
        super.onPause()
        ServiceApp.mInstance?.setConnectivityListener(null)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        ServiceApp.mInstance?.setNetwork(isConnected, this)
    }
}