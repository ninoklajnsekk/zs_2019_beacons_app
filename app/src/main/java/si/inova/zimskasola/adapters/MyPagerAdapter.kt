package si.inova.zimskasola.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


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