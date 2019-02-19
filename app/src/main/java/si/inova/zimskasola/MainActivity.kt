package si.inova.zimskasola

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_main.*
import si.inova.zimskasola.data.Location
import si.inova.zimskasola.data.LocationData

class MainActivity : FragmentActivity(){

    val SPLASH_SCREEN_LENGTH = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting a two second delay before the application continues
        Handler().postDelayed(
            {
                var mainIntent = Intent( this ,LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            ,SPLASH_SCREEN_LENGTH
        )

    }

}


