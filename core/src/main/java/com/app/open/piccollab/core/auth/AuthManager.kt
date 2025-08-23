package com.app.open.piccollab.core.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.app.open.piccollab.core.models.user.UserData
import com.app.open.piccollab.core.utils.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID

private const val TAG = "AuthManager"

object AuthManager {
    fun startGoogleAuthentication(context: Context): Flow<UserData?> {
        return flow {
            try {
                Log.d(TAG, "startGoogleAuthentication() called")

                val nonce = UUID.randomUUID().toString()

                val nonceByte = nonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")

                val bytesDigest = md.digest(nonceByte)
                val hashedNonce = bytesDigest.fold("") { str, it -> str + "%02x".format(it) }

                Log.d(TAG, "startGoogleAuthentication: nonce:  $nonce")


                val getSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
                    serverClientId = WEB_CLIENT_ID
                )
                    .setNonce(hashedNonce)
                    .build()


                val getCredentialRequest: GetCredentialRequest =
                    GetCredentialRequest.Builder()
                        .addCredentialOption(getSignInWithGoogleOption)
                        .build()


                val credentialManager: CredentialManager =
                    CredentialManager.create(context = context)

                val response = credentialManager.getCredential(context, getCredentialRequest)

                handleCredentialResponse(response)

            } catch (e: NoCredentialException) {
                Toast.makeText(context, "No credential found", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startGoogleAuthentication: ", e)
                emit(null)
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startGoogleAuthentication: ", e)
                emit(null)
            }

        }
    }
}

private suspend fun FlowCollector<UserData?>.handleCredentialResponse(
    response: GetCredentialResponse
) {
    val credential = response.credential
    when (credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val credentialData: GoogleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)
                emit(
                    UserData(
                        profilePictureUri = credentialData.profilePictureUri,
                        displayName = credentialData.displayName,
                        phoneNumber = credentialData.phoneNumber,
                        id = credentialData.id,
                        givenName = credentialData.givenName,
                        idToken = credentialData.idToken,
                        familyName = credentialData.familyName,

                        )
                )
            } else {
                Log.d(TAG, "startGoogleAuthentication: $credential")
                emit(null)
            }
        }

        else -> {
            Log.d(TAG, "startGoogleAuthentication: ")
            emit(null)
        }


    }
}