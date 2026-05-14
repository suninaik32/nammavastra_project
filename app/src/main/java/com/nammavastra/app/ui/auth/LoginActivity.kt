package com.nammavastra.app.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.nammavastra.app.MainActivity
import com.nammavastra.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listener for Login to go to MainActivity
        binding.btnLogin.setOnClickListener {
            // For now, we just bypass login and go straight to Main
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Entrance animations
        setupAnimations()
    }

    private fun setupAnimations() {
        // Initial state: hidden and slightly translated down
        binding.cardLoginForm.alpha = 0f
        binding.cardLoginForm.translationY = 50f
        
        binding.tvWelcome.alpha = 0f
        binding.tvWelcome.translationX = -50f
        
        binding.tvSubtitle.alpha = 0f
        binding.tvSubtitle.translationX = -50f

        // Animations
        val welcomeFade = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 0f, 1f)
        val welcomeSlide = ObjectAnimator.ofFloat(binding.tvWelcome, View.TRANSLATION_X, -50f, 0f)

        val subtitleFade = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 0f, 1f)
        val subtitleSlide = ObjectAnimator.ofFloat(binding.tvSubtitle, View.TRANSLATION_X, -50f, 0f)

        val cardFade = ObjectAnimator.ofFloat(binding.cardLoginForm, View.ALPHA, 0f, 1f)
        val cardSlide = ObjectAnimator.ofFloat(binding.cardLoginForm, View.TRANSLATION_Y, 50f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.play(welcomeFade).with(welcomeSlide)
        animatorSet.play(subtitleFade).with(subtitleSlide).after(welcomeFade)
        animatorSet.play(cardFade).with(cardSlide).after(subtitleFade)

        animatorSet.duration = 400
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.start()
    }
}
