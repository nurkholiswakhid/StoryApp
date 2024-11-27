package com.tugas.storyapp.ui.splash

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tugas.storyapp.ViewModelFactory
import com.tugas.storyapp.data.model.UserPreference
import com.tugas.storyapp.databinding.ActivitySplashBinding
import com.tugas.storyapp.ui.login.LoginActivity
import com.tugas.storyapp.ui.main.MainActivity
import com.tugas.storyapp.ui.main.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val welcomeViewModel by viewModels<SplashViewModel> {
            ViewModelFactory(
                UserPreference.getInstance(dataStore)
            )
        }

        var isLogin = false

        welcomeViewModel.getUser().observe(this) { model ->
            isLogin = if (model.isLogin) {
                UserPreference.setToken(model.tokenAuth)
                true
            } else {
                false
            }
        }

        activityScope.launch {
            delay(400L)
            runOnUiThread {
                if (isLogin) {
                    MainActivity.start(this@SplashActivity)
                } else {
                    LoginActivity.start(this@SplashActivity)
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.coroutineContext.cancelChildren()
    }


}
