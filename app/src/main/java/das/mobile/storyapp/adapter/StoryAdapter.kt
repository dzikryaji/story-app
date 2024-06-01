package das.mobile.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import das.mobile.storyapp.data.api.response.Story
import das.mobile.storyapp.databinding.ItemStoryBinding
import das.mobile.storyapp.view.storydetail.StoryDetailActivity

class StoryAdapter: PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { holder.bind(it) }
    }
    class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story){
            binding.apply {
                Glide
                    .with(itemView)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = story.name
                tvItemDescription.text = story.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, StoryDetailActivity::class.java)
                    intent.putExtra(StoryDetailActivity.EXTRA_STORY, story)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "photo"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemDescription, "description")
                        )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}