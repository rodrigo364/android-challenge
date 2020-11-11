package br.com.androidchallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.androidchallenge.R
import kotlinx.android.synthetic.main.activity_user_data.*

class UserDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)
        btn_back?.setOnClickListener {

        }
    }
}