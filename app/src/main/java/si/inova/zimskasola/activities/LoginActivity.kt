package si.inova.zimskasola.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.zimskasola.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null
    var gso: GoogleSignInOptions? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    // Random request code, when onResultAct is triggered you know the call was made from google sign in method
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*
        * Delete before release
        */
        et_username.text.insert(0, "ninoklajnsekk@gmail.com")
        et_password.text.insert(0, "ninoklajnsek")

        /* Initialize variables */
        firebaseAuth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("329347550650-j5fl321svlcfkhleu2sor4rkcbeu1k82.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso!!)

        button_loginWithMail.setOnClickListener {

            // Start loading screen inbetween cred check
            if (!et_username.text.toString().isNullOrBlank() && !et_password.text.toString().isNullOrBlank())
                checkCredentialsAndLogin(et_username.text.toString(), et_password.text.toString())
        }

        cl_loginWithG_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        // When we log in -> finish() or check for logged user
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                val credentials = GoogleAuthProvider.getCredential(account!!.idToken, null)
                firebaseAuth!!.signInWithCredential(credentials).addOnCompleteListener(this) {
                    if (task.isSuccessful) {
                        loginSuccessful()
                    } else {
                        loginUnsuccessful()
                    }
                }

            } catch (e: ApiException) {
                Log.d("GoogleSignInFailed", e.printStackTrace().toString())
            }


        }

    }

    private fun loginUnsuccessful() {
        tv_loginErrorMessage.visibility = View.VISIBLE
    }

    // Check if login was successful return results, if not return null
    fun checkCredentialsAndLogin(email: String, password: String) {

        firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener {

            if (it.isSuccessful) {
                loginSuccessful()
                return@addOnCompleteListener
            }

            loginUnsuccessful()

        }


    }

    fun loginSuccessful() {

        val i = Intent(this, MainSwipeActivity::class.java)
        startActivity(i)
        finish()

    }
}
