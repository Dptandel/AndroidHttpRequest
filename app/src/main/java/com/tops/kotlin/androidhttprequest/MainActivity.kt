package com.tops.kotlin.androidhttprequest

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import com.tops.kotlin.androidhttprequest.databinding.ActivityMainBinding
import com.tops.kotlin.androidhttprequest.models.City
import com.tops.kotlin.androidhttprequest.models.Country
import com.tops.kotlin.androidhttprequest.models.State
import com.tops.kotlin.androidhttprequest.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiToken =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJkaGFybWluMjgwNUBnbWFpbC5jb20iLCJhcGlfdG9rZW4iOiJFd3M4aXBpZE9KZFZWWTJSbGJzSFdGTlNhVUNqbGpFYUJjQTMtOU1Tc2luNTZnNzZURV94d284SUlvbEZpdTgxRmw4In0sImV4cCI6MTczMjUwODk0NX0.TL6TD5LEJ6eanHfM0tMZ3UUzsVOylg7gqSYIWRMm_0Y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCountries()
    }

    private fun fetchCountries() {
        val apiService = RetrofitClient.getService(this)

        apiService.getCountries(apiToken).enqueue(object : Callback<MutableList<Country>> {
            override fun onResponse(
                call: Call<MutableList<Country>>,
                res: Response<MutableList<Country>>
            ) {
                res.body()?.let {
                    val countryAdapter = ArrayAdapter(
                        this@MainActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        it.map { country -> country.country_name }
                    )
                    binding.countrySpinner.setAdapter(countryAdapter)
                    binding.countrySpinner.setOnItemClickListener { adapterView, view, i, l ->
                        val selectedCountryName = it[i].country_name

                        // Reset state and city spinners
                        setDefaultSpinnerHint(binding.stateSpinner, "Select State")
                        setDefaultSpinnerHint(binding.citySpinner, "Select City")

                        fetchStates(selectedCountryName)
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<Country>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fetchStates(selectedCountryName: String) {
        val apiService = RetrofitClient.getService(this)

        apiService.getStates(apiToken, selectedCountryName)
            .enqueue(object : Callback<MutableList<State>> {
                override fun onResponse(
                    call: Call<MutableList<State>>,
                    res: Response<MutableList<State>>
                ) {
                    res.body()?.let {
                        val stateAdapter = ArrayAdapter(
                            this@MainActivity,
                            R.layout.support_simple_spinner_dropdown_item,
                            it.map { state -> state.state_name }
                        )
                        binding.stateSpinner.setAdapter(stateAdapter)
                        binding.stateSpinner.setOnItemClickListener { adapterView, view, i, l ->
                            val selectedStateName = it[i].state_name

                            // Reset city spinner
                            setDefaultSpinnerHint(binding.citySpinner, "Select City")

                            fetchCities(selectedStateName)
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<State>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun fetchCities(selectedStateName: String) {
        val apiService = RetrofitClient.getService(this)

        apiService.getCities(apiToken, selectedStateName)
            .enqueue(object : Callback<MutableList<City>> {
                override fun onResponse(
                    call: Call<MutableList<City>>,
                    res: Response<MutableList<City>>
                ) {
                    res.body()?.let {
                        val cityAdapter = ArrayAdapter(
                            this@MainActivity,
                            R.layout.support_simple_spinner_dropdown_item,
                            it.map { city -> city.city_name }
                        )
                        binding.citySpinner.setAdapter(cityAdapter)
                    }
                }

                override fun onFailure(call: Call<MutableList<City>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun setDefaultSpinnerHint(spinner: AutoCompleteTextView, hint: String) {
        val hintAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, listOf(hint))
        spinner.setAdapter(hintAdapter)
        spinner.setText(hint, false) // Set the default hint text
    }

}