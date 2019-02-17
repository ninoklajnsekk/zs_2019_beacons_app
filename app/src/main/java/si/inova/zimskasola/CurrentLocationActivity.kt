package si.inova.zimskasola

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_current_location.*

class CurrentLocationActivity : AppCompatActivity() {

    private val RC_INTERNET_PERMISION = 5050
    private val RC_FINELOCATION_PERMISSION = 5060

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        cl_progressbar_screen.visibility = (View.GONE)

        checkPermissions()

        scanBeacons()

    }

    fun checkPermissions(){
        // Apparently not needed since it's not a 'dangerous' permission, can be removed if it'll work without it.
        /*
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("permission_check","checking internet permission")
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET))
            {

                Toast.makeText(this,"You need to be connected to the internet for some function to work!",Toast.LENGTH_LONG)

            }
            else{
                Log.d("request_permission","requesting internet permission")
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.INTERNET),RC_INTERNET_PERMISION)
            }

        }
        */

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("permission_check","checking fine_location permission")
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                Toast.makeText(this,"We need access to your location to locate you!",Toast.LENGTH_LONG)
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

                Log.d("LOCATION_PERMISSION","S")
                // Do your thing

            }
            RC_INTERNET_PERMISION -> { }
        }
    }

    fun scanBeacons(){

        val beaconScanner = BeaconScanner(this)
        beaconScanner.subscribe()


    }








}
