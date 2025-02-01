package com.example.lisa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance()

        // Set content with ARScreen using setContent
        setContent {
            ARScreen()
        }

        // Find views for email, password, login button, and signup button
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signupButton = findViewById<Button>(R.id.signup_button)

        // Set click listeners for login and signup buttons
        loginButton.setOnClickListener {
            loginUser(email.text.toString(), password.text.toString())
        }

        signupButton.setOnClickListener {
            registerUser(email.text.toString(), password.text.toString())
        }
    }

    // Function to register a new user with Firebase Authentication
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Registration successful, navigate to DashboardActivity
                Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                // Registration failed, show error message
                Toast.makeText(this, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to log in an existing user with Firebase Authentication
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Login successful, navigate to DashboardActivity
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                // Login failed, show error message
                Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// Composable function to display AR view using ArFragment
@Composable
fun ARScreen() {
    AndroidView(
        factory = { context ->
            // Create a FragmentContainerView dynamically
            FragmentContainerView(context).apply {
                id = View.generateViewId()
                // Replace the container with ArFragment
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(id, ArFragment::class.java, null)
                    .commit()
            }
        },
        modifier = Modifier.fillMaxSize() // Fill the entire screen with AR view
    )
}
