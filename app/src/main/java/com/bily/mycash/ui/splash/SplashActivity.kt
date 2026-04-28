package com.bily.mycash.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.bily.mycash.MainActivity
import com.bily.mycash.MyCashApp
import com.bily.mycash.databinding.ActivitySplashBinding
import com.bily.mycash.ui.setup.SetupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            checkSetupAndBiometric()
        }, 1500)
    }

    private fun checkSetupAndBiometric() {
        val app = application as MyCashApp

        CoroutineScope(Dispatchers.IO).launch {
            val name = app.database.settingsDao().get("bendahara_name")
            val biometricEnabled = app.database.settingsDao().get("biometric_enabled")

            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE

                if (name == null) {
                    // First time setup
                    navigateToSetup()
                } else if (biometricEnabled == "true") {
                    // Show biometric
                    showBiometricPrompt()
                } else {
                    navigateToMain()
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val biometricManager = BiometricManager.from(this)
        val canAuth = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            // Biometric not available, skip
            navigateToMain()
            return
        }

        binding.tvBiometricHint.visibility = View.VISIBLE

        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    navigateToMain()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                        errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        finish()
                    } else {
                        Toast.makeText(this@SplashActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                        navigateToMain() // Fallback
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@SplashActivity, "Sidik jari tidak cocok", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("MyCash")
            .setSubtitle("Verifikasi identitas Anda")
            .setNegativeButtonText("Batal")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToSetup() {
        startActivity(Intent(this, SetupActivity::class.java))
        finish()
    }
}
