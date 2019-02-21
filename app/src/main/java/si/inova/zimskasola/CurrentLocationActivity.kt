package si.inova.zimskasola

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
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

class CurrentLocationActivity : Fragment() {

    private val RC_INTERNET_PERMISION = 5050
    private val RC_FINELOCATION_PERMISSION = 5060

    var beaconObserver: Observer? = null
    var beaconScanner: BeaconScanner? = null

    var location: Location? = null
    var beaconInformation: BeaconInformation? = null

    var currentContext: Context? = null

    lateinit var shimmerLayout: ShimmerFrameLayout

    init {
        currentContext = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        currentContext = context
        var view =  inflater.inflate(R.layout.activity_current_location, container, false)

        shimmerLayout = view.findViewById(R.id.shimmer_view_container)
        shimmerLayout.startShimmer()

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        //setContentView(R.layout.activity_current_location)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        cl_progressbar_screen.visibility = (View.GONE)

        checkPermissions()

        //scanBeacons()

    }

    fun checkPermissions() {

        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("permission_check", "checking fine_location permission")
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
                Toast.makeText(context, "We need access to your location to locate you!", Toast.LENGTH_LONG).show()
            else {
                Log.d("request_permission", "requesting fine_location permission")
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    RC_FINELOCATION_PERMISSION
                )
            }


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_FINELOCATION_PERMISSION -> {

                // Do your thing

            }
            RC_INTERNET_PERMISION -> {
            }
        }
    }

    fun scanBeacons() {

        beaconScanner!!.subscribe()
        beaconScanner!!.addObserver(beaconObserver!!)

    }

    fun updateBeacon(beaconInformation: BeaconInformation) {
        val callback = object : VolleyCallback {
            override fun onSuccessResponse(result: si.inova.zimskasola.data.Location) {
                Log.d("suh", "suh")
                location = result
                updateLocation(beaconInformation)
            }
        }
        val loc = LocationData(context!!, callback)
        loc.getLocationData()
    }

    fun updateLocation(beaconInformation: BeaconInformation) {
        // Update floor and room
        for (floor in location!!.floors) {
            if (floor.floor_id == beaconInformation.place) {
                tv_roomPosition.text = floor.name
                for (room in floor.rooms) {
                    if (room.room_id == beaconInformation.item) {
                        tv_roomName.text = room.name
                        Glide.with(this.activity!!).load(room.image).into(iv_roomImage)
                        lv_currentLocation_items.adapter =
                            DescriptionArrayAdapter(context!!, R.id.lv_currentLocation_items, room.stuff.toList())
                        return
                    }
                }
            }
        }
    }
    fun updateDataset(location: Location, beaconInformation: BeaconInformation){

        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        tv_pre_currently.text = "NAHAJAŠ SE V"
        this.location = location
        this.beaconInformation = beaconInformation
        for (floor in location!!.floors) {
            if (floor.floor_id == beaconInformation.place) {
                tv_roomPosition.text = floor.name
                for (room in floor.rooms) {
                    if (room.room_id == beaconInformation.item) {
                        tv_roomName.text = room.name
                        Glide.with(this.activity!!).load(room.image).placeholder(R.drawable.defaultimage).into(iv_roomImage)
                        lv_currentLocation_items.adapter =
                            DescriptionArrayAdapter(context!!, R.id.lv_currentLocation_items, room.stuff.toList())
                        return
                    }
                }
            }
        }

    }
}
