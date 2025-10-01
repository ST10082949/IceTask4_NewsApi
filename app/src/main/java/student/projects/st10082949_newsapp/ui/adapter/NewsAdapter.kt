package student.projects.st10082949_newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import student.projects.st10082949_newsapp.R
import student.projects.st10082949_newsapp.data.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(
    private var articles: List<Article> = emptyList(),
    private val onItemClick: (Article?) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)

        fun bind(article: Article?) {
            article?.let {
                titleTextView.text = it.title ?: "No title available"
                descriptionTextView.text = it.description ?: "No description available"
                sourceTextView.text = it.source?.name ?: "Unknown source"

                // Format date
                it.publishedAt?.let { dateString ->
                    try {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                        val date = inputFormat.parse(dateString)
                        dateTextView.text = outputFormat.format(date!!)
                    } catch (e: Exception) {
                        dateTextView.text = dateString
                    }
                } ?: run {
                    dateTextView.text = "Date not available"
                }

                // Load image with Glide
                it.urlToImage?.let { imageUrl ->
                    Glide.with(itemView.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.news_placeholder)
                        .error(R.drawable.news_placeholder)
                        .into(newsImageView)
                } ?: run {
                    newsImageView.setImageResource(R.drawable.news_placeholder)
                }

                itemView.setOnClickListener {
                    onItemClick(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles.getOrNull(position))
    }

    override fun getItemCount(): Int = articles.size

    fun updateArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}