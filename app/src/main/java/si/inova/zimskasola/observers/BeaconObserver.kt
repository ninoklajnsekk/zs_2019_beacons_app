package si.inova.zimskasola.observers

import si.inova.zimskasola.BeaconScanner
import si.inova.zimskasola.CurrentLocationActivity

class BeaconObserver(currentLocationActivity: CurrentLocationActivity) : Observer {

    val currentLocationActivity = currentLocationActivity


    override fun action(beaconInformation: BeaconInformation?) {

        currentLocationActivity.updateBeacon(beaconInformation!!)

    }

}

data class BeaconInformation(
    var location: String,
    var place: String,
    var item: String
)