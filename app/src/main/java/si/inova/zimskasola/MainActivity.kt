package si.inova.zimskasola

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

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


