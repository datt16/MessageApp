package io.github.datt16.msg

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.datt16.msg.databinding.ActivitySignUpBinding
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private lateinit var actionBar: androidx.appcompat.app.ActionBar

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "SignUp"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        // プログレスダイアログ(廃止予定) のセットアップ
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("しばらくお待ち下さい")
        progressDialog.setMessage("アカウントを作成しています...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.SignUpBtn.setOnClickListener {
            validateData()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // バックボタン押したとき一つ前のアクティビティへ
        return super.onSupportNavigateUp()
    }

    private fun validateData() {

        // データ取ってくる
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
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
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

                startActivity(Intent(this@SignUpActivity, ProfileActivity::class.java))
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