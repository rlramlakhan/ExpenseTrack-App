package com.rl.expensetrack.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.rl.expensetrack.R
import com.rl.expensetrack.databinding.ActivityMainBinding
import com.rl.expensetrack.model.repositories.AuthRepository
import com.rl.expensetrack.viewmodel.AuthViewModel
import com.rl.expensetrack.viewmodel.AuthViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.topAppBar)
        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        observeViewModel()
    }


    private fun observeViewModel() {
        authViewModel.signOutResult.observe(this) { result ->
            result.onSuccess {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            result.onFailure { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.signOut -> {
                authViewModel.signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}