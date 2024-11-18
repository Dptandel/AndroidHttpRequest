package com.tops.kotlin.androidhttprequest

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJkaGFybWluMjgyMDAyQGdtYWlsLmNvbSIsImFwaV90b2tlbiI6Il80aGJzYmFlSlhyOWVJS3dwTG4tMFNaWS10Y0xxNnRzQnN5T3M2MzI4RnNSX2hWYW1tSmR2aV91dUlyYlRnSGRhbXcifSwiZXhwIjoxNzMxOTkxNzI5fQ.w9Ju0lp013spL3bO-X-Nnopcq7xuoxQnwiBdZ7LN-To"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCountries()
    }

    private fun fetchCountries() {
        val apiService = RetrofitClient.getService(this)

        apiService.getCountries(apiToken).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, res: Response<List<Country>>) {
                if (res.isSuccessful) {
                    val countries = res.body() ?: emptyList()
                    val countryNames = countries.map { it.country_name }

                    val countryAdapter = ArrayAdapter(
                        this@MainActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        countryNames
                    )
                    binding.countrySpinner.adapter = countryAdapter

                    binding.countrySpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedCountryName = countries[position].country_name
                                fetchStates(selectedCountryName)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Handle case where nothing is selected
                            }
                        }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch countries",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
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

        apiService.getStates(apiToken, selectedCountryName).enqueue(object : Callback<List<State>> {
            override fun onResponse(call: Call<List<State>>, res: Response<List<State>>) {
                if (res.isSuccessful) {
                    val states = res.body() ?: emptyList()
                    val stateNames = states.map { it.state_name }

                    val stateAdapter = ArrayAdapter(
                        this@MainActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        stateNames
                    )
                    binding.stateSpinner.adapter = stateAdapter

                    binding.stateSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedStateName = states[position].state_name
                                fetchCities(selectedStateName)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Handle case where nothing is selected
                            }
                        }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch states",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<State>>, t: Throwable) {
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

        apiService.getCities(apiToken, selectedStateName).enqueue(object : Callback<List<City>> {
            override fun onResponse(call: Call<List<City>>, res: Response<List<City>>) {
                if (res.isSuccessful) {
                    val cities = res.body() ?: emptyList()
                    val cityNames = cities.map { it.city_name }

                    val cityAdapter = ArrayAdapter(
                        this@MainActivity,
                        R.layout.support_simple_spinner_dropdown_item,
                        cityNames
                    )
                    binding.citySpinner.adapter = cityAdapter
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to fetch cities",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}