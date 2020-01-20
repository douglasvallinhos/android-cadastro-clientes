package com.example.carolinevallinhos

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {


    //  ATRIBUTOS DA INTERFACE DO USUARIO
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null

    // REFERENCIAS AO BANCO DE DADOS
    private var mDataBaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private val TAG = "CreateAccountActivity"

    //ATRIBUTOS GLOBAIS
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        initialize()
    }

    private fun initialize(){
        etFirstName = findViewById(R.id.et_first_name) as EditText
        etLastName = findViewById(R.id.et_last_name) as EditText
        etEmail = findViewById(R.id.et_email) as EditText
        etPassword = findViewById(R.id.et_password) as EditText
        btnCreateAccount = findViewById(R.id.btn_register) as Button
        mProgressBar = ProgressDialog(this)

        mDatabase = FirebaseDatabase.getInstance()
        mDataBaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnCreateAccount!!.setOnClickListener{createNewAccount()}
    }
    private fun createNewAccount(){
        firstName = etFirstName?.text.toString()
        lastName = etLastName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

        } else{
            Toast.makeText(this,"Todos Campos são obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        mProgressBar!!.setMessage("Registrando Usuario...")
        mProgressBar!!.show()

        mAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this){task ->
            mProgressBar!!.hide()

            if (task.isSuccessful){
                Log.d(TAG,"CreateUserWithEmail:Sucess")

                val userId = mAuth!!.currentUser!!.uid

                verifyEmail();

                val currentUserDb = mDataBaseReference!!.child(userId)
                currentUserDb.child("firstName").setValue(firstName)
                currentUserDb.child("lastName").setValue(lastName)

                updateUserInfoAndUi()
                Toast.makeText(this@CreateAccountActivity, "Sucesso! Verifique sua caixa de entrada de email!", Toast.LENGTH_LONG).show()


            } else {
                Log.w(TAG,"CreateUserWithEmail:Failure",task.exception)
                Toast.makeText(this@CreateAccountActivity, "A autenticação falhou, revise os campos digitados e tente novamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserInfoAndUi(){
        val intent = Intent(this@CreateAccountActivity, CreateAccountActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail(){
        val mUser =  mAuth!!.currentUser;
        mUser!!.sendEmailVerification().addOnCompleteListener(this){
            task ->

            if (task.isSuccessful){
                Toast.makeText(this@CreateAccountActivity, "Verification email sent to "+ mUser.getEmail(),
                    Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "SendEmailVerification", task.exception)
                Toast.makeText(this@CreateAccountActivity, "Failed to send Verification email.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
