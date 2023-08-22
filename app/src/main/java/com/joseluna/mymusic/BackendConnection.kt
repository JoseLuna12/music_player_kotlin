package com.joseluna.mymusic

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class BackendConnection(private val context: Context, private val errorCallback: ((BackendConnection.ErrorResponse) -> Unit)? = null) {
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    data class ErrorResponse(val code: String, val msg: String){
        companion object {
            fun fromVolleyError(error: VolleyError): ErrorResponse{
                return if(error is AuthFailureError){
                    ErrorResponse("401", "User Not Found")
                }else{
                    ErrorResponse("500", "Something Happened")
                }
            }
        }
    }

    fun <T> get(url: String, callBack: (r: String) -> T){
        val stringRequest = StringRequest(Request.Method.GET, url, {result ->
            callBack(result)
        }, {error ->
            errorCallback?.let {
                it(ErrorResponse.fromVolleyError(error))
            }
        })

        queue.add(stringRequest)
    }

}