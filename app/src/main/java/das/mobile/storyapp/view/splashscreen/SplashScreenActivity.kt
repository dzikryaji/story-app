package das.mobile.storyapp.view.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import das.mobile.storyapp.R
import das.mobile.storyapp.view.ViewModelFactory
import das.mobile.storyapp.view.login.LoginActivity
import das.mobile.storyapp.view.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<SplashScreenViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel.getSession().observe(this) {
            runBlocking {
                delay(3000L)
                if (it.isLogin) {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}