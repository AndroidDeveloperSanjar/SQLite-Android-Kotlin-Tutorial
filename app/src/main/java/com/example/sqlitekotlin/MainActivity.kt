package com.example.sqlitekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init(){
        setOnClickListener()
    }

    private fun setOnClickListener(){
        //1
        addRecordBtn.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AddUpdateRecordActivity::class.java
                )
            )
        }

    }
}
