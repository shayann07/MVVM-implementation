package com.shayan.mvvm_login.repository

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginRepository(private val context: Context) {

    private val apiUrl = "https://cricdex.enfotrix.com/api/login"

    fun loginUser(
        phone: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit
    ) {
        val jsonBody = JSONObject().apply {
            put("phone_number", phone)
            put("password", password)
        }

        Log.d("LoginRequest", "Request Body: $jsonBody")  // Add logging to check the data

        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, apiUrl, jsonBody, Response.Listener { response ->
                Log.d("LoginResponse", "Response: $response")

                if (response.has("success") && response.optBoolean("success", false)) {
                    val data = response.optJSONObject("data")
                    val token = data?.optString("token", "")
                    if (token?.isNotEmpty() == true) {
                        onSuccess(token)
                    } else {
                        Log.e("LoginError", "No token received")
                        onError("Login failed: no token received")
                    }
                } else {
                    val message = response.optString("message", "Login Failed!")
                    Log.e("LoginError", "Response Message: $message")
                    onError(message)
                }

            }, Response.ErrorListener { error ->
                Log.e("LoginError", "Error: ${error.message}")
                onError("Error: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> {
                return mapOf("Content-Type" to "application/json")
            }
        }
        Volley.newRequestQueue(context.applicationContext).add(jsonObjectRequest)
    }
}