package com.nammavastra.app

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.iv_logo)
        val appName = findViewById<TextView>(R.id.tv_app_name)
        val tagline = findViewById<TextView>(R.id.tv_tagline)

        // Logo animations: Fade in and Scale up
        val logoFade = ObjectAnimator.ofFloat(logo, View.ALPHA, 0f, 1f)
        val logoScaleX = ObjectAnimator.ofFloat(logo, View.SCALE_X, 0.5f, 1f)
        val logoScaleY = ObjectAnimator.ofFloat(logo, View.SCALE_Y, 0.5f, 1f)

        // Text animations: Fade in
        val textFade = ObjectAnimator.ofFloat(appName, View.ALPHA, 0f, 1f)
        val taglineFade = ObjectAnimator.ofFloat(tagline, View.ALPHA, 0f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(logoFade, logoScaleX, logoScaleY, textFade, taglineFade)
        animatorSet.duration = 1000
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.start()

        // Navigate to LoginActivity after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, com.nammavastra.app.ui.auth.LoginActivity::class.java))
            finish()
        }, 2000)
    }
}
