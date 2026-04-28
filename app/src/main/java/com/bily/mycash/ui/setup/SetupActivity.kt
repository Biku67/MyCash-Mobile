package com.bily.mycash.ui.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bily.mycash.MainActivity
import com.bily.mycash.MyCashApp
import com.bily.mycash.R
import com.bily.mycash.data.db.entity.SettingsEntity
import com.bily.mycash.databinding.ActivitySetupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val className = binding.etClassName.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }
            if (className.isEmpty()) {
                binding.etClassName.error = getString(R.string.error_empty_field)
                return@setOnClickListener
            }

            val app = application as MyCashApp
            CoroutineScope(Dispatchers.IO).launch {
                app.database.settingsDao().set(SettingsEntity("bendahara_name", name))
                app.database.settingsDao().set(SettingsEntity("class_name", className))

                runOnUiThread {
                    Toast.makeText(this@SetupActivity, "Selamat datang, $name!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SetupActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
