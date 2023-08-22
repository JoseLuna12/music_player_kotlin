package com.joseluna.mymusic.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.android.volley.VolleyError
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.joseluna.mymusic.BackendConnection
import com.joseluna.mymusic.Constants
import com.joseluna.mymusic.R
import com.joseluna.mymusic.UserModel
import com.joseluna.mymusic.databinding.FragmentLoginViewBinding

class LoginViewFragment : Fragment() {

    private lateinit var currentView: View

    private lateinit var inputView: TextInputEditText
    private lateinit var buttonView: Button
    private var loggedUser: UserModel? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        currentView = inflater.inflate(R.layout.fragment_login_view, container, false)

        inputView = currentView.findViewById(R.id.username_input)
        buttonView = currentView.findViewById(R.id.login_action)

        buttonView.setOnClickListener {
            loginUser()
        }

        return currentView
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showSnackBar(text: String){
        val snackBar = Snackbar.make(currentView, text, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(
            ContextCompat.getColor(requireContext(), com.google.android.material.R.color.m3_ref_palette_error95)
        )
        snackBar.setTextColor(
            ContextCompat.getColor(requireContext(), com.google.android.material.R.color.m3_ref_palette_error20)
        )
        snackBar.show()
    }

    private fun loginUser(){
        context?.hideKeyboard(currentView)
        val username = inputView.text.toString()
        val client = BackendConnection(requireContext()) { error ->
            showSnackBar(error.msg)
        }
        val userUrl = Constants.ONE_USER_URL(username)

        client.get(userUrl) {result ->
            loggedUser = UserModel.getUserFromJson(result)
            val action = LoginViewFragmentDirections.actionLoginViewFragmentToHomeViewFragment(
                loggedUser!!
            )

            findNavController().navigate(action)
//                .popu(action)
        }
    }
}