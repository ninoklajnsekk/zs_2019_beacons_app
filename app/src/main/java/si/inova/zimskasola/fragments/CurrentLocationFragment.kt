package si.inova.zimskasola.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.zimskasola.R

import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity_current_location.*
import si.inova.zimskasola.adapters.DescriptionArrayAdapter
import si.inova.zimskasola.data.*
import si.inova.zimskasola.observers.BeaconInformation

import si.inova.zimskasola.observers.Observer
import java.util.*
import kotlin.concurrent.schedule

class CurrentLocationFragment : Fragment() {

    private val RC_FINELOCATION_PERMISSION = 5060

    private var beaconObserver: Observer? = null
    private var beaconScanner: BeaconScanner? = null

    private var locationData: Location? = null
    private var newestBeaconInformation: BeaconInformation? = null
    private var beaconInformation: BeaconInformation? = null

    private lateinit var currentContext: Context

    private lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.activity_current_location, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        currentContext = context!!

            swipe_refresh_layout.setOnRefreshListener {

                // On swipe refresh UI
                updateUI()

            }

        shimmerLayout = view!!.findViewById(R.id.shimmer_view_container)
        shimmerLayout.startShimmer()

        checkPermissions()

    }

    private fun startGatheringData() {

        LocationData(currentContext, object : VolleyCallback {
            override fun onSuccessResponse(result: Location) {
                locationData = result
                updateUI()
            }
        })
        beaconScanner = BeaconScanner(currentContext, object : BeaconCallback {
            override fun onSuccessResponse(beaconInfo: BeaconInformation) {
                beaconInformation = beaconInfo
                updateUI()
            }
        }).subscribe()

        startErrorListener()
    }

    private fun startErrorListener() {

        Handler().postDelayed({
            if (beaconInformation == null) {
                errorFetchingData()
            }
            if (locationData == null) {
                errorFetchingData()
            }
        }, 15000)
    }

    private fun errorFetchingLocation() {


    }

    private fun errorFetchingData() {

        ll_default_error.visibility = View.VISIBLE
        shimmerLayout.visibility = View.GONE

    }

    fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(context, "We need access to your locationData to locate you!", Toast.LENGTH_LONG).show()
            else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_FINELOCATION_PERMISSION)
            }
        }
        else{
            startGatheringData()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_FINELOCATION_PERMISSION -> {
                startGatheringData()
            }
        }
    }

    fun scanBeacons() {

        beaconScanner!!.subscribe()
        beaconScanner!!.addObserver(beaconObserver!!)

    }

    fun updateUI() {
        if(newestBeaconInformation != null)
            beaconInformation = newestBeaconInformation
        if (beaconInformation == null || locationData == null)
            return

        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE

        tv_pre_currently.text = "NAHAJAŠ SE V"
        for (floor in locationData!!.floors) {
            if (floor.floor_id == beaconInformation!!.place) {
                tv_roomPosition.text = floor.name
                for (room in floor.rooms) {
                    if (room.room_id == beaconInformation!!.item) {
                        tv_roomName.text = room.name
                        Glide.with(this.activity!!).load(room.image).placeholder(R.drawable.defaultimage)
                            .into(iv_roomImage)
                        lv_currentLocation_items.adapter =
                            DescriptionArrayAdapter(context!!, R.id.lv_currentLocation_items, room.stuff.toList())
                        return
                    }
                }
            }
        }
        newestBeaconInformation = null

    }
}
