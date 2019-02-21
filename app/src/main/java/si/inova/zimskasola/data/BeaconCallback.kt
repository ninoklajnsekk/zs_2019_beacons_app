package si.inova.zimskasola.data

import si.inova.zimskasola.observers.BeaconInformation

public interface BeaconCallback{

    fun onSuccessResponse(beaconInformation: BeaconInformation)

}