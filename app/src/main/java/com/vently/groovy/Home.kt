package com.vently.groovy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.vently.groovy.databinding.FragmentHomeBinding

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ініціалізація Chaquopy
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }

        // Обробник натискання на кнопку
        binding.generateButton.setOnClickListener {
            val site = binding.siteInput.text.toString()
            val masterPassword = binding.masterPasswordInput.text.toString()
            val passLength = binding.passLengthInput.text.toString().toIntOrNull() ?: 12 // За замовчуванням 12

            if (site.isEmpty() || masterPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Заповніть усі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Закриття клавіатури
            closeKeyboard()

            // Виклик Python функції
            val py = Python.getInstance()
            val pyObj = py.getModule("pass") // Ім'я модуля (файлу)
            val password = pyObj.callAttr("generate_password", site, masterPassword, passLength)

            // Відображення згенерованого пароля
            binding.generatedPassword.text = password.toString()
        }

        binding.generatedPassword.setOnClickListener {
            val password = binding.generatedPassword.text.toString()
            if (password != getString(R.string.outputPassword)) {
                copyToClipboard(password)
            }
        }
    }

    private fun closeKeyboard() {
        // Отримання поточного фокуса
        val view = activity?.currentFocus
        if (view != null) {
            // Отримання InputMethodManager
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // Сховати клавіатуру
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Пароль скопійовано в буфер обміну", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}