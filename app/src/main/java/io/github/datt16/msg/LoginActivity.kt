package io.github.datt16.msg

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import io.github.datt16.msg.databinding.ActivityLoginBinding
import java.lang.Exception
import java.util.zip.Inflater

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Login"

        // firebase auth 初期化
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // プログレスダイアログ(廃止予定) のセットアップ
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("しばらくお待ち下さい")
        progressDialog.setMessage("ログインしています...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.haveAccountTv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        binding.loginBtn.setOnClickListener {
            validateData()
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
            finish()
        }
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        // バリデーション
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 適切な形式のメールアドレスじゃなかったとき
            binding.emailTil.error = "正しい形式のメールアドレスを入力してください。"
        } else if (TextUtils.isEmpty(password)) {
            // パスワードの欄が空だった場合
            binding.passwordTil.error = "パスワードを入力してください。"
        } else if (password.length < 6) {
            // パスワードが短かい時
            binding.passwordTil.error = "パスワードは6文字以上に設定して下さい。"
        } else {
            // バリデーション通過　→ サインアップ
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                Log.d(
                    "firebaseSignUp",
                    "Create User: ${result.user?.email.toString()}"
                )
                // ダイアログ閉じる
                progressDialog.dismiss()

                // ユーザーデータ参照 → メアド取ってくる → トーストで通知
                val firebaseUser = firebaseAuth.currentUser
                val mail = firebaseUser?.email
                Toast.makeText(
                    this,
                    "新規登録 \n $mail",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                finish()

            }.addOnFailureListener { e: Exception ->
                Log.d(
                    "firebaseSignUp",
                    "Failure : ${e.message.toString()}"
                )
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "アカウント作成失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}