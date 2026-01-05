package com.example.weatherforecastapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherforecastapp.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.getWeatherButton.setOnClickListener {
            val city = binding.cityEditText.text.toString()
            if (city.isNotEmpty()) {
                getWeatherData(city)
            } else {
                Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeatherData(city: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=a8125da1477c81eb37acfae5c8ed9e38&units=metric"

        val stringRequest = StringRequest(
            Request.Method.GET, 
            url,
            { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val main = jsonResponse.getJSONObject("main")
                    val temp = main.getString("temp")
                    val weather = jsonResponse.getJSONArray("weather").getJSONObject(0)
                    val description = weather.getString("description")
                    val cityName = jsonResponse.getString("name")

                    binding.temperatureTextView.text = "Temperature: $temp°C"
                    binding.conditionTextView.text = "Condition: $description"
                    binding.cityTextView.text = "City: $cityName"
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing weather data", Toast.LENGTH_SHORT).show()
                }
            },
            { 
                Toast.makeText(this, "Error fetching weather data", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(stringRequest)
    }
}
