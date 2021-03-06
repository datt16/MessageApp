package io.github.datt16.msg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.datt16.msg.databinding.ActivityGoogleLoginBinding
import java.lang.Exception


class GoogleLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoogleLoginBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(R.string.default_web_client_id.toString()).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.googleSignInBtn.setOnClickListener {
            Log.d(TAG, "onCreate: begin Google SI")
            signIn()
        }
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google??????????????????????????????????????? ??? firebase??????????????????
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                // ????????????????????????
                Log.d(TAG, "onActivityResult : ${e.message}")
            }
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@GoogleLoginActivity, ProfileActivity::class.java))
            finish()
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            Log.d(
                TAG,
                "firebaseAuthWithGoogleAccount: LoggedIn"
            )

            val firebaseUser = firebaseAuth.currentUser
            val uid = firebaseUser!!.uid
            val email = firebaseUser.email

            Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
            Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $email")

            if (authResult.additionalUserInfo!!.isNewUser) {
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created... -> $email")
                Toast.makeText(
                    this@GoogleLoginActivity,
                    "Account created... \n$email",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User... -> $email")
                Toast.makeText(this@GoogleLoginActivity, "Hello, \n$email", Toast.LENGTH_SHORT)
                    .show()
            }

            startActivity(Intent(this@GoogleLoginActivity, ProfileActivity::class.java))
        }.addOnFailureListener { e ->
            Log.d(
                TAG,
                "firebaseAuthWithGoogleAccount: ${e.message}"
            )
        }
    }

}