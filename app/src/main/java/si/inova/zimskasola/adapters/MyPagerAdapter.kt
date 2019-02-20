package si.inova.zimskasola.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import si.inova.zimskasola.CurrentLocationActivity
import si.inova.zimskasola.activities.RoomListFragment

class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){

    private val NUM_ITEMS: Int = 2

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0-> return CurrentLocationActivity()
            1-> return RoomListFragment()
            2-> Log.d("","")

        }

        return CurrentLocationActivity()
    }

}