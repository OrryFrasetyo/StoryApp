package com.orryfrasetyo.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.orryfrasetyo.storyapp.data.response.ListStoryItem
import com.orryfrasetyo.storyapp.databinding.ActivityDetailStoryBinding

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = intent.getParcelableExtra<ListStoryItem>(DetailStoryActivity.DETAIL_STORY) as ListStoryItem
        setupAction(detailStory)

        supportActionBar?.show()
        supportActionBar?.title = "Detail Story"


    }

    private fun setupAction(detailStory: ListStoryItem) {
        Glide.with(applicationContext)
            .load(detailStory.photoUrl)
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = detailStory.name
        binding.tvDetailDescription.text = detailStory.description
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}
















