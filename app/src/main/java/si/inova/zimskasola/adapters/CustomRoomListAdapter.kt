package si.inova.zimskasola.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.rooms_lv_header.view.*
import si.inova.zimskasola.data.Location

class CustomRoomListAdapter(context: Context, locationList: Location) : BaseAdapter()
{
    // This could've been done better and easier but hey, it's working.
    private lateinit var locationList: Location
    private var counter: Int = 0

    private var layoutInflater: LayoutInflater

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.locationList = locationList

        calculateCounter()
    }

    fun calculateCounter(){

        var count: Int = 0
        for(floor in locationList.floors)
        {

            count += (floor.rooms.size)
        }
        count += locationList.floors.size

        counter = count

    }
    var headerCount: Int = 0
    var dataCount: Int = -1
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        Log.d("insideListView","InsideMan")
        var myConvView: View? = convertView
        var textView: TextView
        if(myConvView == null)
        {
            // Means it's the first one, so it's the headers time.
            if(dataCount<0)
            {
                myConvView = layoutInflater.inflate(R.layout.rooms_lv_header, null)
                textView = myConvView.tv_floorName
                textView.text = locationList.floors.toList()[headerCount].name
                dataCount++
            }
            else{
                myConvView = layoutInflater.inflate(R.layout.rooms_lv_child, null)
                textView = myConvView.findViewById(R.id.tv_roomName)
                textView.text = locationList.floors.toList()[headerCount].rooms.toList()[dataCount].name
                dataCount++
                if(dataCount==locationList.floors.toList()[headerCount].rooms.toList().size)
                {
                    headerCount++
                    dataCount = -1
                }
            }
            myConvView!!.tag = textView
        }



        return myConvView
    }

    override fun getItem(position: Int): Any {

        return 0
        // Implement
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        Log.d("counter",counter.toString())
        return counter
    }



}