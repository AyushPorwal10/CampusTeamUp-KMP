package com.campus.teamup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.feature.auth.util.ActivityProvider
import org.koin.android.ext.android.inject

class AuthActivity : ComponentActivity() {

    private val activityProvider: ActivityProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            Authentication{
                val intent = Intent(this, DashboardActivity::class.java)
                this.startActivity(intent)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityProvider.set(this)
    }
}

