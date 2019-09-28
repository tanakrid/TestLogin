package com.example.testlogin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
class ProfileShop (
    var EmailManager: String? = "",
    var IDShop: String? = "",
    var NameShop: String? = ""
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "EmailManager" to EmailManager,
            "IDShop" to IDShop,
            "NameShop" to NameShop
        )
    }
}