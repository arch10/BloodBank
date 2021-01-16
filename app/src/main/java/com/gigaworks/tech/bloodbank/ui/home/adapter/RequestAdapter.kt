package com.gigaworks.tech.bloodbank.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.RequestItemBinding
import com.gigaworks.tech.bloodbank.domain.model.Request
import java.text.SimpleDateFormat
import java.util.*

class RequestAdapter(
    private val requestList: List<Request>,
    private val listener: OnInteractionListener
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {
    interface OnInteractionListener {
        fun onCardClick(request: Request)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RequestItemBinding.inflate(inflater, parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount() = requestList.size

    inner class RequestViewHolder(private val binding: RequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val sdf = SimpleDateFormat("dd MMMM", Locale.ENGLISH)

        fun bind(request: Request) {
            Glide.with(binding.root)
                .load(request.creatorDp)
                .placeholder(R.drawable.default_profile)
                .into(binding.dp)
            with(binding) {
                val hospitalText = request.hospital ?: ""
                val locationText =
                    if (hospitalText == "") request.city else "$hospitalText, ${request.city}"
                val expiryText = "Valid till ${sdf.format(request.expiry)}"
                name.text = request.creatorName
                bloodType.text = request.bloodType
                created.text = getTime(request.createdOn!!)
                location.text = locationText
                expiry.text = expiryText
                root.setOnClickListener { listener.onCardClick(request) }
            }
        }

        private fun getTime(createdOn: Long): String {
            val currentTime = System.currentTimeMillis()
            val diff = currentTime - createdOn
            return when (val seconds = (diff / 1000).toInt()) {
                in 0..59 -> "$seconds s"
                in 60..3599 -> "${seconds / 60} min"
                in 3600..85399 -> "${seconds / 3600} hr"
                else -> "${seconds / (3600 * 24)} day"
            }
        }
    }
}