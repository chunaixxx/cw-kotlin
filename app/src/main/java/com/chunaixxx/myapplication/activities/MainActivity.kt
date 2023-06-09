package com.chunaixxx.myapplication.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.chunaixxx.myapplication.R
import com.chunaixxx.myapplication.databinding.ActivityMainBinding
import com.chunaixxx.myapplication.fragments.NotesFragment
import com.chunaixxx.myapplication.fragments.TodoFragment
import com.chunaixxx.myapplication.fragments.TrashFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val notesFragment = NotesFragment()
    private val todoFragment = TodoFragment()
    private val trashFragment = TrashFragment()
    private var fragmentManager = supportFragmentManager

    @SuppressLint("UseCompatLoadingForDrawables")

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        window.navigationBarColor = getColor(R.color.black)

        // По умолчанию ставим туду фрагмент
        setCurrentFragment(todoFragment)

        // Обработчик переключения фрагментов в нижнем nav
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miNote -> {
                    setCurrentFragment(notesFragment)
                }
                R.id.miTodo -> {
                    setCurrentFragment(todoFragment)
                }
                R.id.miTrash -> {
                    setCurrentFragment(trashFragment)
                }
            }
            true
        }
    }

    private fun setActionBar() {
        supportActionBar!!.title =
            Html.fromHtml("<font color=\"#FFFFFF\">" + "TODO" + "</font>", 0)
        if (isDarkTheme()) {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.grey,
                    null
                )
            )
        } else {
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.blue,
                    null
                )
            )
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {

        fragmentManager.beginTransaction().apply {
            this.replace(R.id.frameContainer, fragment)
            if (supportFragmentManager.fragments.isNotEmpty()) {
                this.addToBackStack(null)
            }
            this.commit()
        }
        fragmentManager.addOnBackStackChangedListener {
            // find the current fragment and check the string
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frameContainer).toString()
            if (currentFragment.contains("TodoFragment")) {
                binding.bottomNav.menu.getItem(0).isChecked = true
            } else if (currentFragment.contains("NotesFragment")) {
                binding.bottomNav.menu.getItem(1).isChecked = true
            } else if (currentFragment.contains("TrashFragment")) {
                binding.bottomNav.menu.getItem(2).isChecked = true
            }
        }
    }

    private fun isDarkTheme(): Boolean {
        return when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    @SuppressLint("ResourceType")
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (isDarkTheme()) {
            window.statusBarColor = getColor(R.color.grey)
            window.navigationBarColor = getColor(R.color.black)
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.grey,
                    null
                )
            )
            binding.parent.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.black_bg,
                    null
                )
            )
        } else {
            window.statusBarColor = getColor(R.color.blue)
            window.navigationBarColor = getColor(R.color.black)
            supportActionBar!!.setBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.color.blue,
                    null
                )
            )
            binding.parent.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
        }
        super.onConfigurationChanged(newConfig)
    }
}