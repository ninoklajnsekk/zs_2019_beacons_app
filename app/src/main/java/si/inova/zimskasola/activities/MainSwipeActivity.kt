package si.inova.zimskasola.activities

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

import com.google.android.material.tabs.TabLayout

import si.inova.zimskasola.data.BeaconScanner
import si.inova.zimskasola.fragments.CurrentLocationFragment
import si.inova.zimskasola.data.BeaconCallback
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData
import si.inova.zimskasola.data.VolleyCallback
import si.inova.zimskasola.observers.BeaconInformation
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.content.Intent
import com.bumptech.glide.Glide
import com.example.zimskasola.R
import si.inova.zimskasola.fragments.OptionsFragment
import si.inova.zimskasola.fragments.RoomListFragment
import si.inova.zimskasola.services.BackgroundScanService
import java.util.*


class MainSwipeActivity : FragmentActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var myPagerAdapter: MyPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var beaconScanner: BeaconScanner
    private lateinit var locationData: Location

    private var freshBeaconInformation: BeaconInformation? = null
    private var currentBeaconInformation: BeaconInformation? = null

    private val tabTitleList = mutableListOf("Moja lokacija", "Vsi prostori", "Možnosti")
    private val tabIconList =
        mutableListOf(R.drawable.location_selected, R.drawable.all_selected, R.drawable.options_selected)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.zimskasola.R.layout.activity_main_swipe)

        applicationContext.startService((Intent(this, BackgroundScanService::class.java)))

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

        startGatheringData()
        startErrorListener()
    }

    private fun startErrorListener() {

        Timer().schedule(object : TimerTask() {
            override fun run() {

                // If freshbeacon/blabla == null -> error

            }
        }, 10000L)

    }


    private fun startGatheringData() {

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
        var frag = myPagerAdapter.getFragments()[0] as CurrentLocationFragment
        //frag.updateDataset(locationData, beaconInformation)
        iv_refreshButton.visibility = View.INVISIBLE
    }

    private fun locationRecieved() {
        tv_companyName.text = locationData.title
        tv_location.text = locationData.description
    }


    fun setupViewPager() {

        myPagerAdapter = MyPagerAdapter(supportFragmentManager, this)
        myPagerAdapter.addFrag(CurrentLocationFragment(), "Moja lokacija")
        myPagerAdapter.addFrag(RoomListFragment(), "Vsi prostori")
        myPagerAdapter.addFrag(OptionsFragment(), "Možnosti")
        mViewPager.adapter = myPagerAdapter
        mViewPager.offscreenPageLimit = if (myPagerAdapter.count > 1) myPagerAdapter.count - 1 else 1
    }

    fun setupTabIcons() {

        tabTitleList.forEachIndexed { index, title ->
            val tab =
                LayoutInflater.from(this).inflate(com.example.zimskasola.R.layout.custom_tab, null) as ConstraintLayout
            val tv_title = tab.getViewById(R.id.tab) as TextView
            val iv_icon = tab.getViewById(R.id.iv_tabIcon) as ImageView

            tv_title.alpha = 0.6F
            iv_icon.alpha = 0.6F

            tv_title.text = title
            Glide.with(this).load(getDrawable(tabIconList[index])).into(iv_icon)

            tabLayout.getTabAt(index)?.customView = tab
        }
    }


    private fun setupTabListeners() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {


            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab) {

                tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).alpha = 1.0F
                tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab).alpha = 1.0F

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

                tab.customView!!.findViewById<ImageView>(R.id.iv_tabIcon).alpha = 0.6F
                tab.customView!!.findViewById<TextView>(com.example.zimskasola.R.id.tab).alpha = 0.6F


            }

        })
    }

    fun checkInternetConnectivity(): Boolean {

        var connectivityManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected

    }

    override fun onDestroy() {
        beaconScanner.unsubscribe()
        super.onDestroy()
    }

}
