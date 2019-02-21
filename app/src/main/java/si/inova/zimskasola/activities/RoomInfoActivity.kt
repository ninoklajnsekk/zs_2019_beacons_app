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

        var floorIndex = intent.extras.getString("floor")
        var roomIndex = intent.extras.getString("room")

        LocationData(this, object : VolleyCallback {
            override fun onSuccessResponse(result: Location) {
                location = result
                updateLocation(floorIndex,roomIndex)
            }
        })

    }


    fun updateLocation(floorIndex: String, roomIndex: String) {
        // Update floor and room
        for (floor in location!!.floors) {
            if (floor.floor_id == floorIndex) {
                tv_roomPositio.text = floor.name
                for (room in floor.rooms) {
                    if (room.room_id == roomIndex) {
                        tv_roomNam.text = room.name
                        Glide.with(this).load(room.image).into(iv_roomImag)
                        lv_currentLocation_item.adapter =
                            DescriptionArrayAdapter(this, R.id.lv_currentLocation_items, room.stuff.toList())
                        return
                    }
                }
            }
        }
    }
}
