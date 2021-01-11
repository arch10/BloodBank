package com.gigaworks.tech.bloodbank.network.response

import com.gigaworks.tech.bloodbank.network.model.RequestDto

data class RequestsResponse(
    val requests: List<RequestDto>
)