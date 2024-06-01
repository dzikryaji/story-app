package das.mobile.storyapp.view.storydetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import das.mobile.storyapp.data.api.response.Story
import das.mobile.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story

        binding.apply {
            Glide
                .with(this@StoryDetailActivity)
                .load(story.photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = story.name
            tvDetailDescription.text = story.description
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}