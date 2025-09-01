package com.app.open.piccollab.core.models.user

import android.net.Uri
import com.google.gson.Gson

data class UserData(
    val profilePictureUri: Uri? = null,
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val id: String? = null,
    val givenName: String? = null,
    val idToken: String? = null,
    val familyName: String? = null,
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
