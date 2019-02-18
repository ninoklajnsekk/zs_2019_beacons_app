package si.inova.zimskasola.observers

import si.inova.zimskasola.BeaconScanner

interface Observer{

    fun action(beaconInformation: BeaconInformation?)

}