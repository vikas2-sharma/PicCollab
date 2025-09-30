package com.app.open.piccollab.core.auth

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.app.open.piccollab.core.db.datastore.DataStorePref
import com.app.open.piccollab.core.models.user.UserData
import com.app.open.piccollab.core.network.module.RestApiManager
import com.app.open.piccollab.core.utils.WEB_CLIENT_ID
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.services.drive.DriveScopes
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.Date
import java.util.UUID

private const val TAG = "AuthManager"

class AuthManager(private val context: Context, private val dataStorePref: DataStorePref) {
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

    fun getDrivePermission(
        activity: Activity,
        launchIntent: (PendingIntent) -> Unit
    ) {
        Log.d(
            TAG,
            "getDrivePermission() called with: activity = $activity, launchIntent = $launchIntent"
        )
        val requestedScopes: List<Scope> = listOf(Scope(DriveScopes.DRIVE_FILE))
        val authorizationRequest: AuthorizationRequest = AuthorizationRequest.Builder()
            .setRequestedScopes(requestedScopes)
            .build()
        Identity.getAuthorizationClient(activity)
            .authorize(authorizationRequest)
            .addOnSuccessListener { authorizationResult ->
                if (authorizationResult.hasResolution()) {
                    val pendingIntent = authorizationResult.pendingIntent
                    // Access needs to be granted by the user
                    if (pendingIntent != null) {
                        launchIntent(pendingIntent)
                    }
                } else {
                    Log.d(
                        TAG,
                        "getDrivePermission: Access was previously granted, continue with user action"
                    )
                    Log.d(
                        TAG,
                        "getDrivePermission: authorizationResult: ${
                            Gson().toJson(
                                authorizationResult
                            )
                        }"
                    )
                    Log.d(
                        TAG,
                        "getDrivePermission: access token: ${authorizationResult.accessToken}"
                    )
                    Log.d(
                        TAG,
                        "getDrivePermission: result authorizationResult: $authorizationResult"
                    )
                    RestApiManager.accessToken = authorizationResult.accessToken
                    CoroutineScope(Dispatchers.IO).launch {
                        dataStorePref.setAccessToken(
                            authorizationResult.accessToken ?: ""
                        )
                        dataStorePref.setExpiresIn(Date().time + 300_000)
                        delay(1000)
                        dataStorePref.getAccessToken().collect { accessToken ->
                            Log.d(TAG, "getDrivePermission: accessTokenSaved: $accessToken")
                        }
                    }
                }
            }
            .addOnFailureListener { e -> Log.e(TAG, "Failed to authorize", e) }
    }


    suspend fun getNewToken() {
        Log.d(TAG, "getNewToken: ")

        val requestedScopes = listOf(Scope(DriveScopes.DRIVE_FILE))
        val authorizationRequest = AuthorizationRequest.Builder()
            .setRequestedScopes(requestedScopes)
            .build()

        try {
            // Use await() to suspend until the Task is complete
            val result = Identity.getAuthorizationClient(context)
                .authorize(authorizationRequest)
                .await()

            Log.d(TAG, "getNewToken: result: $result")

            dataStorePref.setAccessToken(result.accessToken ?: "")
            dataStorePref.setExpiresIn(Date().time + 300_000) // 5 minutes from now

        } catch (e: Exception) {
            Log.e(TAG, "Error getting new token", e)
            // handle errors properly
        }
    }


    fun logout(activity: Activity) {
        val signInClient = Identity.getSignInClient(activity)
        // This clears the account and forces re-consent next time
        signInClient.signOut()
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

    /*
        fun startAuthorizationIntent(activity: Activity) {
            val startAuthorizationIntent =
                activity.registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
                    try {
                        // extract the result
                        val authorizationResult = Identity.getAuthorizationClient(requireContext())
                            .getAuthorizationResultFromIntent(activityResult.data)
                        // continue with user action
                        */
    /*saveToDriveAppFolder(authorizationResult);*//*

                } catch (ApiException e) {
                    // log exception
                }
            }

    }
*/

}

