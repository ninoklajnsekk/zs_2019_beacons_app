package si.inova.zimskasola.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main_swipe.*

import si.inova.zimskasola.adapters.MyPagerAdapter
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.example.zimskasola.R
import com.google.android.material.tabs.TabLayout

import kotlinx.android.synthetic.main.custom_tab.*
import kotlinx.android.synthetic.main.custom_tab.view.*
import si.inova.zimskasola.CurrentLocationActivity


class MainSwipeActivity : FragmentActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var myPagerAdapter: MyPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.zimskasola.R.layout.activity_main_swipe)

        mViewPager = findViewById(com.example.zimskasola.R.id.pager)
        setupViewPager()


        tabLayout = tabs as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        setupTabIcons()
        setupTabListeners()

    }



    fun setupViewPager(){

        myPagerAdapter = MyPagerAdapter(supportFragmentManager, this)
        myPagerAdapter.addFrag(CurrentLocationActivity(),"Moja lokacija")
        myPagerAdapter.addFrag(RoomListFragment(),"Vsi prostori")
        myPagerAdapter.addFrag(OptionsFragment(),"Možnosti")
        mViewPager.adapter = myPagerAdapter
    }

    fun setupTabIcons(){

        val tabOne = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as ConstraintLayout
        var tv_tabTitle= tabOne.getViewById(R.id.tab) as TextView
        var iv_tabIcon = tabOne.getViewById(R.id.iv_tabIcon) as ImageView

        tv_tabTitle.text = "Moja lokacija"
        tv_tabTitle.setTextColor(Color.parseColor("#000000"))
        iv_tabIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.location_selected))
        tabLayout.getTabAt(0)?.customView = tabOne


        val tabTwo = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as ConstraintLayout
        tv_tabTitle= tabTwo.getViewById(R.id.tab) as TextView
        iv_tabIcon = tabTwo.getViewById(R.id.iv_tabIcon) as ImageView
        tv_tabTitle.setTextColor(Color.parseColor("#999999"))

        tv_tabTitle.text = "Moja lokacija"
        iv_tabIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.all_unselected))
        tabLayout.getTabAt(1)?.customView = tabTwo


        /*val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as TextView
        tabThree.text = "Moja lokacija"
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, com.example.zimskasola.R.drawable.options_unselected, 0, 0)
        tabLayout.getTabAt(0)?.customView = tabThree*/

        val tabThree = LayoutInflater.from(this).inflate(R.layout.custom_tab, null) as ConstraintLayout
        tv_tabTitle= tabThree.getViewById(R.id.tab) as TextView
        iv_tabIcon = tabThree.getViewById(R.id.iv_tabIcon) as ImageView
        tv_tabTitle.setTextColor(Color.parseColor("#999999"))

        tv_tabTitle.text = "Moja lokacija"
        iv_tabIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.options_unselected))
        tabLayout.getTabAt(2)?.customView = tabThree

    }


    private fun setupTabListeners() {
        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0-> {
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon)
                            .setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.location_unselected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#999999"))
                    }
                    1->{
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).setImageDrawable(ContextCompat.getDrawable(baseContext,R.drawable.all_unselected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#999999"))
                    }
                    2->{
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).setImageDrawable(ContextCompat.getDrawable(baseContext,R.drawable.options_unselected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#999999"))
                    }

                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0->{
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).setImageDrawable(ContextCompat.getDrawable(baseContext,R.drawable.location_selected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#000000"))
                    }
                    1->{
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).setImageDrawable(ContextCompat.getDrawable(baseContext,R.drawable.all_selected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#000000"))
                    }
                    2->{
                        tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).setImageDrawable(ContextCompat.getDrawable(baseContext,R.drawable.options_selected))
                        tab.customView!!.findViewById<TextView>(R.id.tab).setTextColor(Color.parseColor("#000000"))
                    }

                }
            }

        })
    }

}
