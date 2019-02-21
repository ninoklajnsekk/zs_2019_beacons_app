package si.inova.zimskasola.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.rooms_lv_header.view.*
import si.inova.zimskasola.data.Location
import android.R
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import si.inova.zimskasola.activities.RoomInfoActivity
import si.inova.zimskasola.data.Floor
import kotlin.coroutines.coroutineContext
import kotlin.math.floor


class CustomRoomListAdapter(context: Context, locationList: Location) : BaseAdapter() {
    // This could've been done better and easier but hey, it's working.
    private lateinit var locationList: Location
    private var counter: Int = 0
    private lateinit var context: Context
    private var layoutInflater: LayoutInflater

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.locationList = locationList

        calculateCounter()
        this.context = context
    }

    fun calculateCounter() {

        var count: Int = 0
        for (floor in locationList.floors) {

            count += (floor.rooms.size)
        }
        count += locationList.floors.size

        counter = count

    }

    var s: Int = 0
    var d: Int = 0


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var v: View? = convertView

        val type: Int = getItemViewType(position)

        var viewHolder: ViewHolder
        if (v == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            if (type == 0) {
                v = inflater.inflate(com.example.zimskasola.R.layout.rooms_lv_header, null)
                var t: TextView = v!!.findViewById(com.example.zimskasola.R.id.tv_floorName)
                viewHolder = ViewHolder(t)
                v.tag = (viewHolder)

            } else {
                v = inflater.inflate(com.example.zimskasola.R.layout.rooms_lv_child, null)
                var t: TextView = v!!.findViewById(com.example.zimskasola.R.id.tv_roomName)

                viewHolder = ViewHolder(t)
                v.tag = (viewHolder)
            }
        }
        else{
            viewHolder = v.tag as ViewHolder

        }
        viewHolder.textView.text = (getCorrectPosition(position))

        return v
    }
    override fun getItem(position: Int): Any {

        return 0
        // Implement
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        Log.d("counter", counter.toString())
        return counter
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    fun getCorrectPosition(position: Int): String {

        var floorIndex = -1
        var roomIndex = -1
        var positionsLeft: Int = position
        loop@ for(floor in locationList.floors) {
            roomIndex = -1
            floorIndex++
            for(room in floor.rooms) {
                if(positionsLeft == 0) {

                    break@loop
                }
                else
                {
                    positionsLeft--
                    roomIndex++
                }
            }
            if(positionsLeft == 0) {

                break@loop
            }
            positionsLeft--

        }

        if(roomIndex == -1){
            Log.d("Position", position.toString())
            Log.d("Floor name", locationList.floors.toMutableList()[floorIndex].name)

            return locationList.floors.toMutableList()[floorIndex].name
        }
        else
        {
            this.s = floorIndex
            this.d = roomIndex
            return locationList.floors.toMutableList()[floorIndex].rooms.toMutableList()[roomIndex].name
        }
    }

    fun getCorrectIndex(position: Int): List<Int> {

        var floorIndex = 0
        var roomIndex = -1
        var positionsLeft: Int = position
        loop@ for (floor in locationList.floors) {
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
            return listOf(floorIndex, roomIndex)
        }

    }


    override fun getItemViewType(position: Int): Int {
        var cc = 0
        for (floor in locationList.floors) {

            if (cc++ == position) return 0 //Means type is floor
            else {
                cc += floor.rooms.size
            }
            if (cc > position) return 1 //Means type is room
        }
        return 0
    }
}

internal class ViewHolder (textView: TextView) {
    var textView: TextView = textView
}