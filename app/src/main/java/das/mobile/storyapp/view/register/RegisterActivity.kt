package das.mobile.storyapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import das.mobile.storyapp.data.Result
import das.mobile.storyapp.data.api.request.RegisterRequest
import das.mobile.storyapp.databinding.ActivityRegisterBinding
import das.mobile.storyapp.view.ViewModelFactory
import das.mobile.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private  val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction(){
        binding.apply {
            btnRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                val request = RegisterRequest(name, email, password)

                viewModel.register(request).observe(this@RegisterActivity){ result ->
                    when (result) {
                        is Result.Loading -> {
                            progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            Toast.makeText(this@RegisterActivity, result.error, Toast.LENGTH_SHORT).show()
                            progressIndicator.visibility = View.GONE
                        }
                        is Result.Success -> {
                            Toast.makeText(applicationContext, result.data, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                            progressIndicator.visibility = View.GONE
                        }
                    }
                }
            }

            tvLogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30F, 30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1F).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1F).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1F).setDuration(500)
        val etName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1F).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1F).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1F).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, tvName, etName, tvEmail, etEmail, tvPassword, etPassword, register, tvLogin)
            startDelay = 100
            start()
        }
    }
}