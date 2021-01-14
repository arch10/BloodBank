package com.gigaworks.tech.bloodbank.ui.requestdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.gigaworks.tech.bloodbank.R
import com.gigaworks.tech.bloodbank.databinding.ActivityRequestDetailsBinding
import com.gigaworks.tech.bloodbank.domain.model.Request
import com.gigaworks.tech.bloodbank.ui.base.BaseActivity
import com.gigaworks.tech.bloodbank.util.visible
import java.text.SimpleDateFormat
import java.util.*

class RequestDetails : BaseActivity<ActivityRequestDetailsBinding>() {

    private val sdf = SimpleDateFormat("dd MMMM", Locale.ENGLISH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpView()
    }

    private fun setUpView() {
        binding.toolbar.setNavigationOnClickListener { handleBackPress() }
        val request = intent.extras?.getParcelable<Request>(REQUEST)
        val currentUserUid = firebaseAuth.currentUser?.uid
        if(request?.creatorUid == currentUserUid) {
            binding.callBtn.visible(false)
        }
        Glide.with(this)
            .load(request?.creatorDp)
            .placeholder(R.drawable.default_profile)
            .into(binding.dp)
        with(binding) {
            if(request != null) {
                val descText = request.desc?:""
                if(descText == "") {
                    desc.visible(false)
                }
                val hospitalText = request.hospital ?: ""
                val locationText =
                    if (hospitalText == "") request.city else "$hospitalText, ${request.city}"
                val expiryText = "Valid till ${sdf.format(request.expiry)}"
                name.text = request.creatorName
                bloodType.text = request.bloodType
                desc.text = descText
                created.text = getTime(request.createdOn!!)
                location.text = locationText
                expiry.text = expiryText
                callBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${request.countryCode}${request.phone}")
                    }
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }
            }
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

    private fun handleBackPress() {
        finish()
    }

    override fun getViewBinding(inflater: LayoutInflater) =
        ActivityRequestDetailsBinding.inflate(inflater)

    companion object {
        const val REQUEST = "request"
    }
}