package das.mobile.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.api.request.LoginRequest
import das.mobile.storyapp.databinding.ActivityLoginBinding
import das.mobile.storyapp.view.ViewModelFactory
import das.mobile.storyapp.view.main.MainActivity
import das.mobile.storyapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.apply {
            tvRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                val request = LoginRequest(email, password)

                viewModel.login(request).observe(this@LoginActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            progressIndicator.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            Toast.makeText(
                                this@LoginActivity,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                            progressIndicator.visibility = View.GONE
                        }

                        is Result.Success -> {
                            Toast.makeText(applicationContext, result.data, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1F).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1F).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1F).setDuration(500)
        val etEmail =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val tvPassword =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1F).setDuration(500)
        val etPassword =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvEmail, etEmail, tvPassword, etPassword, login, tvRegister)
            startDelay = 100
            start()
        }
    }
}