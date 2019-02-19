package si.inova.zimskasola

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_current_location.*
import si.inova.zimskasola.adapters.DescriptionArrayAdapter
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback
import si.inova.zimskasola.observers.BeaconInformation
import si.inova.zimskasola.observers.BeaconObserver
import si.inova.zimskasola.observers.Observer

class CurrentLocationActivity : FragmentActivity() {

    private val RC_INTERNET_PERMISION = 5050
    private val RC_FINELOCATION_PERMISSION = 5060

    val beaconObserver: Observer = BeaconObserver(this)
    val beaconScanner: BeaconScanner = BeaconScanner(this)

    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        cl_progressbar_screen.visibility = (View.GONE)

        checkPermissions()

        scanBeacons()

    }

    fun checkPermissions(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("permission_check","checking fine_location permission")
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(this,"We need access to your location to locate you!",Toast.LENGTH_LONG).show()
            else
            {
                Log.d("request_permission","requesting fine_location permission")
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),RC_FINELOCATION_PERMISSION)
            }


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            RC_FINELOCATION_PERMISSION -> {

                // Do your thing

            }
            RC_INTERNET_PERMISION -> { }
        }
    }

    fun scanBeacons(){

        beaconScanner.subscribe()
        beaconScanner.addObserver(beaconObserver)

    }

    fun updateBeacon(beaconInformation: BeaconInformation) {
        val database = FirebaseFirestore.getInstance()
        val callback = object:VolleyCallback {
            override fun onSuccessResponse(result: si.inova.zimskasola.data.Location) {
                Log.d("suh", "suh")
                location = result
                updateLocation(beaconInformation)
            }
        }
        val loc = LocationData(this, callback)
        loc.getLocationData()




        /*database.collection("/locations").document("${beaconInformation.location}").get().addOnSuccessListener { documentSnapshot ->
            locationInfo = documentSnapshot.toObject(Location::class.java)
            updateLocationInfo()
        }

        database.collection("/locations/${beaconInformation.location}/places/").document("${beaconInformation.place}").get().addOnSuccessListener { documentSnapshot ->
            placeInfo = documentSnapshot.toObject(Place::class.java)
            updatePlaceInfo()
        }


        database.collection("/locations/${beaconInformation.location}/places/${beaconInformation.place}/description_items/").addSnapshotListener{data,error->
            descriptionItems = data!!.documents.map{ it.toObject(DescriptionItem::class.java)}.toMutableList()
            updateListView()
        }*/

    }
    fun updateLocation(beaconInformation: BeaconInformation){
        // Update location
        tv_companyName.text = location?.title
        tv_location.text = location?.description

        // Update floor and room
        for(floor in location!!.floors)
        {
            if(floor.floor_id==beaconInformation.place)
            {
                tv_roomPosition.text = floor.name
                for(room in floor.rooms)
                {
                    if(room.room_id == beaconInformation.item){
                        tv_roomName.text = room.name
                        Glide.with(this).load(room.image).into(iv_roomImage)
                        lv_currentLocation_items.adapter = DescriptionArrayAdapter(this,R.id.lv_currentLocation_items,room.stuff.toList())
                        return
                    }
                }
            }
        }
    }
}
