package student.projects.st10082949_newsapp.data.repository

import android.util.Log
import student.projects.st10082949_newsapp.data.api.RetrofitClient
import student.projects.st10082949_newsapp.data.model.NewsResponse
import retrofit2.Response

class NewsRepository {

    private val TAG = "NewsRepository"

    // Helper function to create empty response
    private fun createEmptyResponse(): NewsResponse {
        return NewsResponse(
            status = null,
            totalResults = 0,
            articles = emptyList(),
            message = null,
            data = null,
            results = null,
            publishers = null,  // Add this
            article = null      // Add this
        )
    }

    // Test API connection using search publishers endpoint
    suspend fun testApiConnection(): Boolean {
        return try {
            val response = RetrofitClient.newsApiService.searchPublishers("news")
            Log.d(TAG, "API test response: ${response.isSuccessful}")
            if (response.isSuccessful) {
                Log.d(TAG, "API connection successful")
            } else {
                Log.d(TAG, "API connection failed: ${response.code()} - ${response.message()}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "API connection test failed: ${e.message}")
            false
        }
    }

    suspend fun getTopHeadlines(): Response<NewsResponse> {
        return try {
            Log.d(TAG, "Fetching top headlines using trending news")
            // Use trending news as top headlines
            RetrofitClient.newsApiService.getTrendingTopics("General")
        } catch (e: Exception) {
            Log.e(TAG, "getTopHeadlines failed: ${e.message}")
            // Fallback to search
            Response.success(createEmptyResponse())
        }
    }

    suspend fun searchNews(query: String): Response<NewsResponse> {
        return try {
            Log.d(TAG, "Searching for: $query")
            RetrofitClient.newsApiService.searchArticles(query)
        } catch (e: Exception) {
            Log.e(TAG, "searchNews failed: ${e.message}")
            Response.success(createEmptyResponse())
        }
    }

    suspend fun getTrendingTopics(topic: String? = null): Response<NewsResponse> {
        return try {
            Log.d(TAG, "Getting trending topics for: $topic")
            RetrofitClient.newsApiService.getTrendingTopics(topic)
        } catch (e: Exception) {
            Log.e(TAG, "getTrendingTopics failed: ${e.message}")
            Response.success(createEmptyResponse())
        }
    }

    suspend fun getRandomArticle(topic: String? = null): Response<NewsResponse> {
        return try {
            Log.d(TAG, "Getting random article for topic: $topic")
            RetrofitClient.newsApiService.getRandomArticle(topic = topic)
        } catch (e: Exception) {
            Log.e(TAG, "getRandomArticle failed: ${e.message}")
            Response.success(createEmptyResponse())
        }
    }
}