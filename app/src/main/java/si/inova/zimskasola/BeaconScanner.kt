package si.inova.zimskasola

import android.content.Context
import android.util.Log
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import si.inova.zimskasola.observers.BeaconInformation
import si.inova.zimskasola.observers.Observer

private const val LOG_TAG = "BeaconScanner"

class BeaconScanner(context: Context) : MessageListener() {

    val observerList : MutableList<Observer> =  ArrayList()

    val context: Context = context
    var currentBeaconId:String? = null
    var currentBeaconInformation: BeaconInformation? = null


    override fun onFound(message: Message) {
        Log.d(LOG_TAG, "BeaconFound")
        super.onFound(message)

        currentBeaconId = message.content.toString()

        var attachmentList: MutableList<String> = String(message.content).split(";").toMutableList()

        // Probably the worst way to do things, but it'll do. Gotta get that technical dept higher!

        var location: String = attachmentList[0].substring("location:".length,attachmentList[0].length)
        var place: String = attachmentList[1].substring("place:".length,attachmentList[1].length)
        var item: String = attachmentList[2].substring("item:".length,attachmentList[2].length)


        currentBeaconInformation = BeaconInformation(location,place,item)

        notifyObservers()
    }

    override fun onLost(message: Message) {

        super.onLost(message)
        currentBeaconId = null
        Log.d("beaconLost",message.content.toString())

    }

    fun subscribe(){

        Log.i("beacon_subscribe", "Subscribing.");

        val options = SubscribeOptions.Builder().setStrategy(Strategy.BLE_ONLY).build()
        Nearby.getMessagesClient(context).subscribe(this,options)

    }
    fun unsubscribe(){

        Nearby.getMessagesClient(context).unsubscribe(this)

    }


    fun addObserver(observer: Observer){

        observerList.add(observer)

    }
    fun notifyObservers(){

        for(observer in observerList)
        {
            observer.action(currentBeaconInformation)
        }
    }


}