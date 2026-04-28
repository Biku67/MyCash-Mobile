package com.bily.mycash.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bily.mycash.MyCashApp
import com.bily.mycash.databinding.FragmentAccountBinding
import com.bily.mycash.data.db.entity.SettingsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MyCashApp

        // Load profile
        CoroutineScope(Dispatchers.IO).launch {
            val name = app.database.settingsDao().get("bendahara_name") ?: "Bendahara"
            val className = app.database.settingsDao().get("class_name") ?: "Kelas"

            withContext(Dispatchers.Main) {
                binding.tvProfileName.text = name
                binding.tvProfileClass.text = className
                binding.tvAvatar.text = name.firstOrNull()?.uppercase() ?: "B"
            }
        }

        // Observe student count
        app.studentRepository.studentCount.observe(viewLifecycleOwner) { count ->
            binding.tvStudentCount.text = "$count siswa"
        }

        // Menu clicks
        binding.menuStudents.setOnClickListener {
            // TODO: Navigate to student list
        }

        binding.menuReport.setOnClickListener {
            // TODO: Navigate to report
        }

        binding.menuBackup.setOnClickListener {
            // TODO: Backup & Restore
        }

        binding.menuAbout.setOnClickListener {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Tentang MyCash")
                .setMessage("MyCash v1.0\nAplikasi Pencatatan Kas Kelas\n\nDibuat oleh Biku")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
