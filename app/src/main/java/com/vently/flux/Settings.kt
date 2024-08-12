package com.vently.flux

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class Settings : Fragment() {

    private lateinit var themeSwitch: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Заміни requireContext().MODE_PRIVATE на Context.MODE_PRIVATE
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        themeSwitch = view.findViewById(R.id.themeSwitch)
        themeSwitch.isChecked = isDarkMode

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            // Зберігаємо стан Switch у SharedPreferences
            with(sharedPreferences.edit()) {
                putBoolean("isDarkMode", isChecked)
                apply()
            }

            // Перезавантаження тільки поточного фрагмента
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(this.id, Settings())
                .commitAllowingStateLoss()
        }



        return view
    }
}
