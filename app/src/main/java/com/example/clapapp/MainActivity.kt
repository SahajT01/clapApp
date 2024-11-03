package com.example.clapapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clapapp.ui.theme.BackgroundColor

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var clapCount by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClapCounterScreen(clapCount)
        }

        // Initialize the sensor manager and proximity sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(this, "No Proximity Sensor Found!", Toast.LENGTH_SHORT).show()
            finish() // Close the app if there is no proximity sensor
        }
    }

    override fun onResume() {
        super.onResume()
        proximitySensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            // Check if the hand is close to the sensor
            if (event.values[0] < (proximitySensor?.maximumRange ?: 0f)) {
                // Increment the clap count
                clapCount++
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used for this app
    }
}

@Composable
fun ClapCounterScreen(clapCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text(text = "Clap App", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(36.dp))


        // Logo Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            val image = painterResource(id = R.drawable.logo)
            Image(painter = image, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(36.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text(text = "Clap Count: $clapCount", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClapCounterScreenPreview() {
    ClapCounterScreen(clapCount = 0)
}


