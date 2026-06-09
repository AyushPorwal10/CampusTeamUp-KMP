package com.campus.teamup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.feature.auth.util.ActivityProvider
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val activityProvider: ActivityProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onResume() {
        super.onResume()
        activityProvider.set(this)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
