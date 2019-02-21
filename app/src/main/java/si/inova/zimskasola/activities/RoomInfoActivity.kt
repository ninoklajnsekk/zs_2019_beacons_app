package si.inova.zimskasola.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_current_location.*
import kotlinx.android.synthetic.main.activity_room_info.*
import si.inova.zimskasola.adapters.DescriptionArrayAdapter
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback
import si.inova.zimskasola.observers.BeaconInformation

class RoomInfoActivity : FragmentActivity() {

    lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_info)

        iv_x_close.setOnClickListener{
               super.onBackPressed()
        }

        intent.extras.get("floor")
        var floorIndex: String = intent.extras.get("floor").toString()
        var roomIndex: String = intent.extras.get("room").toString()

        LocationData(this, object : VolleyCallback {
            override fun onSuccessResponse(result: Location) {
                location = result
                updateLocation(floorIndex.toInt(),roomIndex.toInt())
            }
        })

    }


    fun updateLocation(floorIndex: Int, roomIndex: Int) {
        // Update floor and room
        tv_roomPositio.text = location!!.floors.toMutableList()[floorIndex].name
        Log.d("ime",location!!.floors.toMutableList()[floorIndex].name)
        tv_roomNam.text = location!!.floors.toMutableList()[floorIndex].rooms.toMutableList()[roomIndex].name
        Glide.with(this).load(location!!.floors.toMutableList()[floorIndex].rooms.toMutableList()[roomIndex].image).into(iv_roomImag)
        lv_currentLocation_item.adapter = DescriptionArrayAdapter(this, R.id.lv_currentLocation_items, location!!.floors.toMutableList()[floorIndex].rooms.toMutableList()[roomIndex].stuff.toList())
        return
    }
}
