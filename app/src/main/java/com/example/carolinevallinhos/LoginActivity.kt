package com.example.carolinevallinhos
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import android.view.Window
import android.view.WindowManager
import android.os.Build
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.carolinevallinhos.control.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    //ATRIBUTOS GLOBAIS

    private var email: String? = null
    private var password: String? = null

    //INTERFACE DO USUARIO

    private var tvForgotPassword: TextView? = null
    private var etEmail: TextView? = null
    private var etPassword: TextView? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null
    private var cbSalvarEmailSenha: CheckBox? = null

    // REFERENCIAS AO BANCO FIREBASE

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColorTo(R.color.colorPrimary)
        }
        initialize()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            val name = getString("NAME", "")
            val key = getString("KEY", "")
            etEmail?.setText(name)
            etPassword?.setText(key)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun Window.setStatusBarColorTo(color: Int){
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = ContextCompat.getColor(baseContext, color)
    }
    private fun initialize(){
        tvForgotPassword = findViewById(R.id.tv_forgot_password) as TextView
        etEmail = findViewById(R.id.et_email) as EditText
        etPassword = findViewById(R.id.et_password) as EditText
        btnLogin = findViewById(R.id.btn_login) as Button
        btnCreateAccount = findViewById(R.id.btn_register_account) as Button
        mProgressBar = ProgressDialog(this)
        cbSalvarEmailSenha = findViewById(R.id.checkBox) as CheckBox

        mAuth = FirebaseAuth.getInstance()

        tvForgotPassword!!.setOnClickListener{ startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))}

        btnCreateAccount!!.setOnClickListener{ startActivity(Intent(this@LoginActivity, CreateAccountActivity::class.java))}

        btnLogin!!.setOnClickListener{loginUser()}

    }

    private fun loginUser(){
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()



        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgressBar!!.setMessage("Verificando Usuário...")
            mProgressBar!!.show()
            Log.d(TAG, "Login do usuario")

            mAuth!!.signInWithEmailAndPassword(email!!,password!!).addOnCompleteListener(this){
                task ->
                mProgressBar!!.hide()

                if (task.isSuccessful){
                    Log.d(TAG, "Logado com sucesso")

                    if(cbSalvarEmailSenha!!.isChecked){

                        val pref2 = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = pref2.edit()

                        editor
                            .putString("NAME",etEmail?.text.toString())
                            .putString("KEY",etPassword?.text.toString())
                            .apply()
                    }

                    updateUi()
                }else{
                    Log.e(TAG, "Erro ao logar", task.exception)
                    Toast.makeText(this@LoginActivity, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this,"Os campos são obrigatórios!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUi(){
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}

