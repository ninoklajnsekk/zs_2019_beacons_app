package si.inova.zimskasola

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.zimskasola.R
import kotlinx.android.synthetic.main.activity_current_location.*

class CurrentLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        //cl_progressbar_screen.visibility = (View.GONE)
    }
}
