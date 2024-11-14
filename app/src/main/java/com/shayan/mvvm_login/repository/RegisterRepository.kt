package com.shayan.mvvm_login.repository

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.shayan.mvvm_login.model.ModelUser
import org.json.JSONObject

class RegisterRepository(private val context: Context) {

    private val apiUrl = "https://cricdex.enfotrix.com/api/register"

    fun registerUser(user: ModelUser, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val jsonBody = JSONObject().apply {
            put("first_name", user.firstName)
            put("last_name", user.lastName)
            put("phone_number", user.phone)
            put("password", user.password)
            put("c_password", user.cPass)
        }

        val jsonObjectRequest = object :
            JsonObjectRequest(Method.POST, apiUrl, jsonBody, Response.Listener { response ->
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
