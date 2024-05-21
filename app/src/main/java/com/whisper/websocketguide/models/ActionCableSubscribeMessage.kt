package com.whisper.websocketguide.models

import com.google.gson.annotations.SerializedName

data class ActionCableSubscribeMessage(
    @SerializedName("command") var command: String,
    @SerializedName("identifier") var identifier: ActionCableIdentifier
) {
     fun toSubscriptionCommand() = "{\"command\":\"$command\", \"identifier\":\"$identifier\"}"
}

data class ActionCableIdentifier(
    @SerializedName("channel") var channel: String,
    @SerializedName("identifiers") var identifiers: Map<String, String> = emptyMap()
) {
    override fun toString(): String {
        return (mapOf("channel" to channel) + identifiers).entries.joinToString(prefix = "{", postfix = "}") { (k, v) -> "\\\"$k\\\": \\\"$v\\\"" }}
}
