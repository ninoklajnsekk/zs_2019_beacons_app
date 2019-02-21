package si.inova.zimskasola.activities

import android.graphics.Color
import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main_swipe.*

import si.inova.zimskasola.adapters.MyPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

import com.google.android.material.tabs.TabLayout

import si.inova.zimskasola.BeaconScanner
import si.inova.zimskasola.CurrentLocationActivity
import si.inova.zimskasola.data.BeaconCallback
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback
import si.inova.zimskasola.observers.BeaconInformation
import android.R


class MainSwipeActivity : FragmentActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var myPagerAdapter: MyPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var beaconScanner: BeaconScanner
    private lateinit var locationData: Location

    private var freshBeaconInformation: BeaconInformation? = null
    private var currentBeaconInformation: BeaconInformation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.zimskasola.R.layout.activity_main_swipe)

        freshBeaconInformation = null
        currentBeaconInformation = null

        /*************************Waste*********************************/
        mViewPager = findViewById(com.example.zimskasola.R.id.pager)
        setupViewPager()
        tabLayout = tabs as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
        setupTabIcons()
        setupTabListeners()
        /***************************************************************/

        LocationData(this, object : VolleyCallback {
            override fun onSuccessResponse(result: Location) {
                locationData = result
                locationRecieved()
            }
        })

        beaconScanner = BeaconScanner(this, object : BeaconCallback {
            override fun onSuccessResponse(beaconInf: BeaconInformation) {
                if (currentBeaconInformation == null) {
                    currentBeaconInformation = beaconInf
                    updateUI(currentBeaconInformation as BeaconInformation)
                } else {
                    if (currentBeaconInformation != beaconInf) {
                        freshBeaconInformation = beaconInf
                        notifyUser()
                    }
                }


            }

        })
        beaconScanner.subscribe()
    }

    private fun notifyUser() {

        iv_refreshButton.visibility = View.VISIBLE
        iv_refreshButton.setOnClickListener {
            updateUI(freshBeaconInformation as BeaconInformation)
        }

    }

    private fun updateUI(beaconInformation: BeaconInformation) {
        currentBeaconInformation = beaconInformation
        var frag = myPagerAdapter.getFragments()[0] as CurrentLocationActivity
        frag.updateDataset(locationData, beaconInformation)
        iv_refreshButton.visibility = View.INVISIBLE
    }

    private fun locationRecieved() {
        tv_companyName.text = locationData.title
        tv_location.text = locationData.description
    }


    fun setupViewPager() {

        myPagerAdapter = MyPagerAdapter(supportFragmentManager, this)
        myPagerAdapter.addFrag(CurrentLocationActivity(), "Moja lokacija")
        myPagerAdapter.addFrag(RoomListFragment(), "Vsi prostori")
        myPagerAdapter.addFrag(OptionsFragment(), "Možnosti")
        mViewPager.adapter = myPagerAdapter
        mViewPager.offscreenPageLimit = if (myPagerAdapter.getCount() > 1) myPagerAdapter.getCount() - 1 else 1
    }

    fun setupTabIcons() {

        val tabOne =
            LayoutInflater.from(this).inflate(com.example.zimskasola.R.layout.custom_tab, null) as ConstraintLayout
        var tv_tabTitle = tabOne.getViewById(com.example.zimskasola.R.id.tab) as TextView
        var iv_tabIcon = tabOne.getViewById(com.example.zimskasola.R.id.iv_tabIcon) as ImageView

        tv_tabTitle.text = "Moja lokacija"
        tv_tabTitle.setTextColor(Color.parseColor("#000000"))
        iv_tabIcon.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                com.example.zimskasola.R.drawable.location_selected
            )
        )
        tabLayout.getTabAt(0)?.customView = tabOne


        val tabTwo =
            LayoutInflater.from(this).inflate(com.example.zimskasola.R.layout.custom_tab, null) as ConstraintLayout
        tv_tabTitle = tabTwo.getViewById(com.example.zimskasola.R.id.tab) as TextView
        iv_tabIcon = tabTwo.getViewById(com.example.zimskasola.R.id.iv_tabIcon) as ImageView
        tv_tabTitle.setTextColor(Color.parseColor("#999999"))

        tv_tabTitle.text = "Vsi prostori"
        iv_tabIcon.setImageDrawable(ContextCompat.getDrawable(this, com.example.zimskasola.R.drawable.all_unselected))
        tabLayout.getTabAt(1)?.customView = tabTwo

        val tabThree =
            LayoutInflater.from(this).inflate(com.example.zimskasola.R.layout.custom_tab, null) as ConstraintLayout
        tv_tabTitle = tabThree.getViewById(com.example.zimskasola.R.id.tab) as TextView
        iv_tabIcon = tabThree.getViewById(com.example.zimskasola.R.id.iv_tabIcon) as ImageView
        tv_tabTitle.setTextColor(Color.parseColor("#999999"))

        tv_tabTitle.text = "Možnosti"
        iv_tabIcon.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                com.example.zimskasola.R.drawable.options_unselected
            )
        )
        tabLayout.getTabAt(2)?.customView = tabThree

    }


    private fun setupTabListeners() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.location_unselected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#999999"))
                    }
                    1 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.all_unselected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#999999"))
                    }
                    2 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.options_unselected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#999999"))
                    }

                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.location_selected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#000000"))
                    }
                    1 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.all_selected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#000000"))
                    }
                    2 -> {
                        tab.customView!!.findViewById<ImageView>(com.example.zimskasola.R.id.iv_tabIcon)
                            .setImageDrawable(
                                ContextCompat.getDrawable(
                                    baseContext,
                                    com.example.zimskasola.R.drawable.options_selected
                                )
                            )
                        tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab)
                            .setTextColor(Color.parseColor("#000000"))
                    }

                }
            }

        })
    }

    override fun onDestroy() {
        beaconScanner.unsubscribe()
        super.onDestroy()
    }

}
