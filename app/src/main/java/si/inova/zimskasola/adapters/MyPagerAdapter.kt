package si.inova.zimskasola.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){

    private val NUM_ITEMS: Int = 3

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItem(position: Int): Fragment {

        when(position){
            0-> Log.d("","")
            1-> Log.d("","")
            2-> Log.d("","")

        }

        return Fragment()
    }

}