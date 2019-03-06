package si.inova.zimskasola.observers

import si.inova.zimskasola.fragments.CurrentLocationFragment

class BeaconObserver(currentLocationActivity: CurrentLocationFragment) : Observer {

    val currentLocationActivity = currentLocationActivity


    override fun action(beaconInformation: BeaconInformation?) {



    }

}

data class BeaconInformation(
    var location: String,
    var place: String,
    var item: String
)