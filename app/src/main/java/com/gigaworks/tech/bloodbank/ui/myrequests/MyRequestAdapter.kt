package com.gigaworks.tech.bloodbank.ui.myrequests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gigaworks.tech.bloodbank.databinding.MyRequestItemBinding
import com.gigaworks.tech.bloodbank.domain.model.Request

class MyRequestAdapter(
    private val requestList: List<Request>,
    private val listener: OnInteractionListener
) : RecyclerView.Adapter<MyRequestAdapter.MyRequestViewHolder>() {

    interface OnInteractionListener {
        fun onCardClick(request: Request)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyRequestItemBinding.inflate(inflater, parent, false)
        return MyRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRequestViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount() = requestList.size

    inner class MyRequestViewHolder(private val binding: MyRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(request: Request) {
            val hospitalText = request.hospital ?: ""
            val locationText =
                if (hospitalText == "") request.city else "$hospitalText, ${request.city}"
            with(binding) {
                bloodType.text = request.bloodType
                location.text = locationText
                created.text = getTime(request.createdOn!!)
                status.text = getStatus(request.expiry)
            }

            binding.root.setOnClickListener { listener.onCardClick(request) }
        }

        private fun getStatus(expiry: Long): String {
            val currentTime = System.currentTimeMillis()
            return if (currentTime < expiry) {
                "In Progress"
            } else {
                "Completed"
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