package si.inova.zimskasola

import android.content.Context
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*

private const val LOG_TAG = "BeaconScanner"

class BeaconScanner(context: Context) : MessageListener() {

    val context: Context = context



    override fun onFound(message: Message?) {
        super.onFound(message)
        Log.d("beaconFound","found")
    }

    override fun onLost(message: Message?) {
        super.onLost(message)
        Log.d("beaconLost","lost")
    }

    fun subscribe(){

        Nearby.getMessagesClient(context).subscribe(this)

    }
    fun unsubscribe(){

        Nearby.getMessagesClient(context).unsubscribe(this)

    }


}