package com.orryfrasetyo.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.orryfrasetyo.storyapp.R
import com.orryfrasetyo.storyapp.data.ResultState
import com.orryfrasetyo.storyapp.databinding.ActivityMainBinding
import com.orryfrasetyo.storyapp.view.ViewModelFactory
import com.orryfrasetyo.storyapp.view.addstory.AddStoryActivity
import com.orryfrasetyo.storyapp.view.maps.MapsActivity
import com.orryfrasetyo.storyapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()

        binding.fabUpload.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.getSession().observe(this) {
            if (!it.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                getData()
                setupAction()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    viewModel.logout()
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.maps -> {
                lifecycleScope.launch {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupAction() {
        lifecycleScope.launch {
            viewModel.getStory().observe(this@MainActivity) { story ->
                when (story) {
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                    }

                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = story.error
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getData() {
        val adapter = MainAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.stories.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

}













