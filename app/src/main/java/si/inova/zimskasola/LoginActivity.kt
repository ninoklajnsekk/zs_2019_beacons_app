package si.inova.zimskasola

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.zimskasola.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    val anus: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        cl_loginWithG_button.setOnClickListener{

            // Not yet implemented
            Log.d("login with G","Not yet implemented")

        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun checkCredentials(){

        val user = firebaseAuth?.currentUser


    }

}
