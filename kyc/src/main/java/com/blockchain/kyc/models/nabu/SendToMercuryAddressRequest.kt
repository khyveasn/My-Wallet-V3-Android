package com.blockchain.kyc.models.nabu

data class SendToMercuryAddressRequest(val currency: String)

data class SendToMercuryAddressResponse(
    val address: String,
    val currency: String,
    val state: State // "PENDING" | "ACTIVE" | "BLOCKED"
)

enum class State {
    PENDING, ACTIVE, BLOCKED
}