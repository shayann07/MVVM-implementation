package com.shayan.mvvm_login.repository

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shayan.mvvm_login.model.ModelUser
import org.json.JSONObject

class AuthRepository(private val context: Context) {

    private val loginUrl = "https://cricdex.enfotrix.com/api/login"
    private val registerUrl = "https://cricdex.enfotrix.com/api/register"

    fun loginUser(
        phone: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit
    ) {
        val jsonBody = JSONObject().apply {
            put("phone_number", phone)
            put("password", password)
        }

        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, loginUrl, jsonBody, Response.Listener { response ->
                if (response.optBoolean("success", false)) {
                    val token = response.optJSONObject("data")?.optString("token", "")
                    if (!token.isNullOrEmpty()) {
                        onSuccess(token)
                    } else {
                        onError("Login failed: no token received")
                    }
                } else {
                    onError(response.optString("message", "Login Failed!"))
                }
            }, Response.ErrorListener { error ->
                onError("Error: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> =
                mapOf("Content-Type" to "application/json")
        }
        Volley.newRequestQueue(context.applicationContext).add(jsonObjectRequest)
    }

    fun registerUser(user: ModelUser, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val jsonBody = JSONObject().apply {
            put("first_name", user.firstName)
            put("last_name", user.lastName)
            put("phone_number", user.phone)
            put("password", user.password)
            put("c_password", user.cPass)
        }

        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, registerUrl, jsonBody, Response.Listener { response ->
                val isSuccess = response.optBoolean("success", false)
                if (isSuccess) {
                    onSuccess()
                } else {
                    onError(response.optString("message", "Registration Failed!"))
                }
            }, Response.ErrorListener { error ->
                onError("Error: ${error.message}")
            }) {
            override fun getHeaders(): Map<String, String> =
                mapOf("Content-Type" to "application/json")
        }
        Volley.newRequestQueue(context.applicationContext).add(jsonObjectRequest)
    }
}
