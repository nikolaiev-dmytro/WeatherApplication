package com.nikolaiev.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nikolaiev.weatherapplication.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}