package student.projects.st10082949_newsapp.data.model

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("country") val country: String?
)

data class Article(
    @SerializedName("source") val source: Source?,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String?,
    @SerializedName("content") val content: String?,

    // Alternative field names that might be used
    @SerializedName("image") val image: String?,
    @SerializedName("published") val published: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("article_id") val articleId: String?,

    // For article content endpoint
    @SerializedName("text") val text: String?,
    @SerializedName("html") val html: String?
)

data class NewsResponse(
    @SerializedName("status") val status: String?,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<Article>?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Article>?,
    @SerializedName("results") val results: List<Article>?,

    // For publisher search
    @SerializedName("publishers") val publishers: List<Source>?,

    // For single article responses
    @SerializedName("article") val article: Article?
) {
    fun extractArticles(): List<Article> {
        return articles ?: data ?: results ?: listOfNotNull(article) ?: emptyList()
    }

    companion object {
        fun empty(): NewsResponse {
            return NewsResponse(
                status = null,
                totalResults = 0,
                articles = emptyList(),
                message = null,
                data = null,
                results = null,
                publishers = null,
                article = null
            )
        }
    }
}