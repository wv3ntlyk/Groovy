package com.vently.flux
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vently.flux.databinding.ActivityMainBinding
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Перевірка теми перед встановленням вмісту активності
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Перевірка, чи активність не відновлюється після зміни теми
        if (savedInstanceState == null) {
            replaceFragment(Home())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(Home())
                R.id.settings -> replaceFragment(Settings())
                R.id.saved -> replaceFragment(Saved())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
