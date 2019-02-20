package si.inova.zimskasola.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.fragment_room_list.*
import si.inova.zimskasola.adapters.CustomRoomListAdapter
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback

class RoomListFragment : Fragment() {

    lateinit var location: Location
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_room_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callback = object: VolleyCallback {
            override fun onSuccessResponse(result: si.inova.zimskasola.data.Location) {
                Log.d("onActivityRoom", "suh")
                updateListview(result)
            }
        }
        val loc = LocationData(context!!, callback)
        loc.getLocationData()



    }

    fun updateListview(location: Location){
        lv_roomList.adapter = CustomRoomListAdapter(this.context!!,location)
    }
}
