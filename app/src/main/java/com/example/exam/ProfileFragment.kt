package com.example.exam

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.exam.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var _vb: FragmentProfileBinding? = null
    private val vb get() = _vb!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _vb = FragmentProfileBinding.bind(view)

        vb.btnOrders.setOnClickListener { show("Мои заказы") }
        vb.btnCards.setOnClickListener { show("Медицинские карты") }
        vb.btnAddresses.setOnClickListener { show("Мои адреса") }
        vb.btnSettings.setOnClickListener { show("Настройки") }
        vb.btnLogout.setOnClickListener { show("Выход") }
    }
    private fun show(label: String) = Toast.makeText(requireContext(), label, Toast.LENGTH_SHORT).show()
    override fun onDestroyView() { _vb = null; super.onDestroyView() }
}
