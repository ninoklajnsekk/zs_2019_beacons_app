package si.inova.zimskasola.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import si.inova.zimskasola.CurrentLocationActivity
import si.inova.zimskasola.activities.RoomListFragment
import android.text.Spannable
import android.text.style.ImageSpan
import android.text.SpannableString
import android.graphics.drawable.Drawable
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.example.zimskasola.R


class MyPagerAdapter(fragmentManager: FragmentManager, context: Context) : FragmentStatePagerAdapter(fragmentManager){

    private var context: Context
    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var tabTitles : MutableList<String> = mutableListOf()

    init {
        this.context = context
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList.toMutableList()[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return tabTitles.toMutableList()[position]

    }

    fun addFrag(fragment: Fragment, title: String)
    {
        fragmentList.add(fragment)
        tabTitles.add(title)
    }
    fun getFragments(): MutableList<Fragment>{
        return fragmentList
    }
    override fun getCount(): Int {
        return fragmentList.size
    }
}