package com.example.exam

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    lateinit var shopVM: ShopVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopVM = androidx.lifecycle.ViewModelProvider(this)[ShopVM::class.java]

        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navCart = findViewById<LinearLayout>(R.id.navCart)
        val navProfile = findViewById<LinearLayout>(R.id.navProfile)

        navHome.setOnClickListener { selectTab(0) }
        navCart.setOnClickListener { selectTab(1) }
        navProfile.setOnClickListener { selectTab(2) }

        if (savedInstanceState == null) {
            selectTab(0)
        }
    }

    private fun selectTab(index: Int) {
        val icHome = findViewById<ImageView>(R.id.icHome)
        val icCart = findViewById<ImageView>(R.id.icCart)
        val icProfile = findViewById<ImageView>(R.id.icProfile)
        val tvHome = findViewById<TextView>(R.id.tvHome)
        val tvCart = findViewById<TextView>(R.id.tvCart)
        val tvProfile = findViewById<TextView>(R.id.tvProfile)

        val activeColor = ContextCompat.getColor(this, R.color.nav_active)
        val inactiveColor = ContextCompat.getColor(this, R.color.nav_inactive)

        icHome.setTint(if (index == 0) activeColor else inactiveColor)
        icCart.setTint(if (index == 1) activeColor else inactiveColor)
        icProfile.setTint(if (index == 2) activeColor else inactiveColor)
        tvHome.setTextColor(if (index == 0) activeColor else inactiveColor)
        tvCart.setTextColor(if (index == 1) activeColor else inactiveColor)
        tvProfile.setTextColor(if (index == 2) activeColor else inactiveColor)

        val fragment: Fragment = when (index) {
            0 -> HomeFragment()
            1 -> CartFragment()
            2 -> ProfileFragment()
            else -> HomeFragment()
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun ImageView.setTint(color: Int) {
        this.setColorFilter(color)
    }
}
