package si.inova.zimskasola.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.fragment_room_list.*
import si.inova.zimskasola.activities.RoomInfoActivity
import si.inova.zimskasola.adapters.CustomRoomListAdapter
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback

class RoomListFragment : Fragment() {

    private var locationData: Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_room_list, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val callback = object : VolleyCallback {
            override fun onSuccessResponse(result: si.inova.zimskasola.data.Location) {
                locationData = result
                updateListview()
            }
        }

        val loc = LocationData(context!!, callback)
        loc.getLocationData()
        errorListener()

    }

    fun errorListener(){
            Handler().postDelayed({
                if (locationData == null) {
                    onErrorFetchingData()
                }
            }, 5000)
    }

    private fun onErrorFetchingData() {
        roomlist_ll_default_error.visibility = View.VISIBLE
        tv_prostoriTV.visibility = View.INVISIBLE
        lv_roomList.visibility = View.INVISIBLE
    }

    fun updateListview() {

        roomlist_shimmer_view_container.visibility = View.INVISIBLE

        lv_roomList.adapter = CustomRoomListAdapter(this.context!!, locationData!!)
        lv_roomList.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            var list = getCorrectIndex(i, locationData!!)


            var i: Intent = Intent(context, RoomInfoActivity::class.java)
            //var bundle: Bundle = Bundle()

            i.putExtra("floor", list.toMutableList()[0].toString())
            i.putExtra("room", list.toMutableList()[1].toString())

            context!!.startActivity(i)


        }
    }
}

fun getCorrectIndex(position: Int, location: Location): List<Int> {

    var floorIndex = 0
    var roomIndex = -1
    var positionsLeft: Int = position
    loop@ for (floor in location.floors) {
        roomIndex = -1
        floorIndex++
        for (room in floor.rooms) {
            if (positionsLeft == 0) {

                break@loop
            } else {
                positionsLeft--
                roomIndex++
            }
        }
        if (positionsLeft == 0) {

            break@loop
        }
        positionsLeft--

    }


    if (roomIndex == -1) {

        return listOf(0, 0)
        //return locationList.floors.toMutableList()[floorIndex].name
    } else {
        return listOf(floorIndex-1, roomIndex)
    }

}
