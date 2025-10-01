package student.projects.st10082949_newsapp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import student.projects.st10082949_newsapp.data.model.NewsResponse

interface NewsApiService {

    // 1. Search News
    @GET("v2/search/articles")
    suspend fun searchArticles(
        @Query("query") query: String,
        @Query("language") language: String = "en"
    ): Response<NewsResponse>

    // 2. Trending News
    @GET("v2/trendings")
    suspend fun getTrendingTopics(
        @Query("topic") topic: String? = null,
        @Query("language") language: String = "en"
    ): Response<NewsResponse>

    // 3. Get Article Content (returns different response type)
    @GET("v2/article")
    suspend fun getArticleContent(
        @Query("url") url: String,
        @Query("type") type: String = "plaintext"
    ): Response<NewsResponse>

    // 4. Get Random Article
    @GET("v2/article/random")
    suspend fun getRandomArticle(
        @Query("language") language: String = "en",
        @Query("topic") topic: String? = null,
        @Query("type") type: String = "plaintext"
    ): Response<NewsResponse>

    // 5. Search Publishers (for testing connectivity)
    @GET("v2/search/publishers")
    suspend fun searchPublishers(
        @Query("query") query: String = "news",
        @Query("language") language: String = "en"
    ): Response<NewsResponse>
}