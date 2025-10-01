package student.projects.st10082949_newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import student.projects.st10082949_newsapp.data.model.Article
import student.projects.st10082949_newsapp.data.model.Source
import student.projects.st10082949_newsapp.data.repository.NewsRepository
import student.projects.st10082949_newsapp.databinding.ActivityMainBinding
import student.projects.st10082949_newsapp.ui.adapter.NewsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private val newsRepository = NewsRepository()

    companion object {
        private const val TAG = "NewsApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "App started")
        setupRecyclerView()

        // Test API connection
        testApiConnection()

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                searchNews(query)
            } else {
                loadTopHeadlines()
            }
        }

       
    }

    private fun testApiConnection() {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isConnected = newsRepository.testApiConnection()

                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (isConnected) {
                        Log.d(TAG, "API connection successful.")
                        Toast.makeText(
                            this@MainActivity,
                            "API connected successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadTopHeadlines()
                    } else {
                        showError("API connection failed. Using sample data.")
                        Log.d(TAG, "Loading sample data due to API failure")
                        loadSampleData()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    showError("Connection test failed: ${e.message}")
                    loadSampleData()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(emptyList()) { article ->
            Toast.makeText(this, "Clicked: ${article?.title}", Toast.LENGTH_SHORT).show()
        }

        binding.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }
    }

    private fun loadTopHeadlines() {
        showLoading(true)
        Log.d(TAG, "Loading trending news as top headlines...")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsRepository.getTopHeadlines()
                withContext(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let { newsResponse ->
                            val articles = newsResponse.extractArticles()
                            if (articles.isNotEmpty()) {
                                newsAdapter.updateArticles(articles)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Loaded ${articles.size} trending articles",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d(TAG, "Successfully loaded ${articles.size} articles from API")
                            } else {
                                showEmptyState("No trending articles found")
                                loadSampleData()
                            }
                        }
                    } else {
                        showError("API returned empty response. Using sample data.")
                        loadSampleData()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    showError("Error: ${e.message}. Using sample data.")
                    loadSampleData()
                }
            }
        }
    }

    private fun searchNews(query: String) {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsRepository.searchNews(query)
                withContext(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let { newsResponse ->
                            val articles = newsResponse.extractArticles()
                            if (articles.isNotEmpty()) {
                                newsAdapter.updateArticles(articles)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Found ${articles.size} articles for '$query'",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showEmptyState("No articles found for '$query'")
                            }
                        }
                    } else {
                        showError("Search failed. Please try again.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    showError("Search error: ${e.message}")
                }
            }
        }
    }

    // You can still keep this method if you want to call it from elsewhere
    private fun loadRandomArticle() {
        showLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsRepository.getRandomArticle()
                withContext(Dispatchers.Main) {
                    showLoading(false)

                    if (response.isSuccessful && response.body() != null) {
                        response.body()?.let { newsResponse ->
                            val articles = newsResponse.extractArticles()
                            if (articles.isNotEmpty()) {
                                newsAdapter.updateArticles(articles)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Loaded random article",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showEmptyState("No random article found")
                            }
                        }
                    } else {
                        showError("Failed to get random article")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    showError("Random article error: ${e.message}")
                }
            }
        }
    }

    // Fallback method with sample data
    private fun loadSampleData() {
        val sampleArticles = listOf(
            Article(
                source = Source(null, "Tech News", null, null, null, null),
                author = "John Doe",
                title = "Welcome to News App",
                description = "This is sample data while we configure the API connection",
                url = "https://example.com",
                urlToImage = null,
                publishedAt = "2024-01-01",
                content = "This is sample content that appears when the API is not available.",
                image = null,
                published = null,
                summary = null,
                articleId = null,
                text = null,
                html = null
            ),
            Article(
                source = Source(null, "Science Daily", null, null, null, null),
                author = "Jane Smith",
                title = "Sample Article: API Configuration",
                description = "Please check your API key and endpoint configuration",
                url = "https://example.com",
                urlToImage = null,
                publishedAt = "2024-01-01",
                content = "Make sure your RapidAPI subscription is active and endpoints are correct.",
                image = null,
                published = null,
                summary = null,
                articleId = null,
                text = null,
                html = null
            )
        )

        newsAdapter.updateArticles(sampleArticles)
        Toast.makeText(this, "Loaded sample data", Toast.LENGTH_LONG).show()
        Log.d(TAG, "Sample data loaded with ${sampleArticles.size} articles")
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e(TAG, message)
    }

    private fun showEmptyState(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        newsAdapter.updateArticles(emptyList())
    }
}