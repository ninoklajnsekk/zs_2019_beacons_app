package si.inova.zimskasola.activities

import android.os.Bundle

import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.example.zimskasola.R

import si.inova.zimskasola.adapters.MyPagerAdapter

class MainSwipeActivity : FragmentActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var myPagerAdapter: MyPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_swipe)

        mViewPager = findViewById(R.id.pager)
        myPagerAdapter = MyPagerAdapter(supportFragmentManager)
        mViewPager.adapter = myPagerAdapter
    }

}
