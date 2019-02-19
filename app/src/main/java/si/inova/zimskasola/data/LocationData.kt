package si.inova.zimskasola.data

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import si.inova.zimskasola.CurrentLocationActivity

class LocationData(context: Context, callback: VolleyCallback) {

    // Currently working for a object based json file. If you expect to have an array of locations there should be a json with an array and use JsonArrayRequest.
    val callback = callback
    val context = context
    val JSON_URL: String = "https://firebasestorage.googleapis.com/v0/b/zs-beacons-2019.appspot.com/o/25022c4a-3035-11e9-bb6a-a5c92278bce1.json?alt=media&token=4607d4e9-c453-4ce8-9a9a-3b3d76ce244b"
    var request: JsonObjectRequest? = null

    fun getLocationData() {
        var location: Location? = null

        request = JsonObjectRequest(JSON_URL, null, Response.Listener { response ->

            var locationJsonObject: JSONObject? = response
            var gson = Gson()
            location = gson.fromJson(locationJsonObject.toString(), Location::class.java)
            Log.d("noterror","noterror")
            callback.onSuccessResponse(location!!)

        }, Response.ErrorListener{ Log.d("getLocationData()-Error", it.message) })


        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

}

data class Location(
    var title: String,
    var description: String,
    var floors: MutableCollection<Floor>

) {
    constructor() : this("", "", mutableListOf(Floor()))
}

data class Floor(
    var floor_id: String,
    var name: String,
    var rooms: MutableCollection<Room>

) {
    constructor() : this("", "", mutableListOf(Room()))
}

data class Room(
    var room_id: String,
    var beacon_id: String,
    var name: String,
    var image: String,
    var stuff: MutableCollection<Stuff>

) {
    constructor() : this("", "", "", "", mutableListOf(Stuff()))
}

data class Stuff(
    var stuff_id: String,
    var name: String,
    var category: String,
    var description: String,
    var icon: String

) {
    constructor() : this("", "", "", "", "")
}
