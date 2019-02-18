package si.inova.zimskasola

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_current_location.*
import si.inova.zimskasola.adapters.DescriptionArrayAdapter
import si.inova.zimskasola.observers.BeaconInformation
import si.inova.zimskasola.observers.BeaconObserver
import si.inova.zimskasola.observers.Observer

class CurrentLocationActivity : AppCompatActivity() {

    private val RC_INTERNET_PERMISION = 5050
    private val RC_FINELOCATION_PERMISSION = 5060

    val beaconObserver: Observer = BeaconObserver(this)
    val beaconScanner: BeaconScanner = BeaconScanner(this)

    var descriptionItems: MutableList<DescriptionItem?> = mutableListOf()
    var placeInfo: Place? = null

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

        database.collection("/locations/${beaconInformation.location}/places/").document("${beaconInformation.place}").get().addOnSuccessListener { documentSnapshot ->
            placeInfo = documentSnapshot.toObject(Place::class.java)
            updatePlaceInfo()
        }


        database.collection("/locations/${beaconInformation.location}/places/${beaconInformation.place}/description_items/").addSnapshotListener{data,error->
            descriptionItems = data!!.documents.map{ it.toObject(DescriptionItem::class.java)}.toMutableList()
            updateListView()
        }

    }

    fun updatePlaceInfo(){

        tv_roomName.text = placeInfo?.name
        tv_roomPosition.text = placeInfo?.floor
        Glide.with(this).load(placeInfo?.image).into(iv_roomImage)

    }

    fun updateListView(){

        Log.d("UpdateListView", descriptionItems.size.toString())
        lv_currentLocation_items.adapter = DescriptionArrayAdapter(this,R.id.lv_currentLocation_items,descriptionItems.toList())

    }

    data class DescriptionItem(
        var subtitle: String,
        var title: String,
        var type: String,
        var type_icon: String
    ){
        constructor() : this("","","","")
    }

    data class Place(
        var floor: String,
        var image: String,
        var name: String
    ){
        constructor() : this("","","")
    }


}
