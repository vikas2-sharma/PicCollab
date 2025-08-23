package com.app.open.piccollab.core.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetPasswordOption
import androidx.credentials.GetPublicKeyCredentialOption
import androidx.credentials.exceptions.NoCredentialException
import com.app.open.piccollab.core.utils.BundlePrinter
import com.app.open.piccollab.core.utils.WEB_CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID

private const val TAG = "AuthManager"

object AuthManager {
    fun startGoogleAuthentication(context: Context): Flow<Boolean> {
        return flow {
            try {
                Log.d(TAG, "startGoogleAuthentication() called")
                /*val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
                        serverClientId = WEB_CLIENT_ID
                    ).setNonce(UUID.randomUUID().toString())
                        .build()

                    Log.d(TAG, "startGoogleAuthentication: ${signInWithGoogleOption.serverClientId}")*/

                val nonce = UUID.randomUUID().toString()

                val nonceByte = nonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")

                val bytesDigest = md.digest(nonceByte)
                val hashedNonce = bytesDigest.fold("") { str, it -> str + "%02x".format(it) }

                Log.d(TAG, "startGoogleAuthentication: nonce:  $nonce")
                /*val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(WEB_CLIENT_ID)
                    .setAutoSelectEnabled(false)
                    // nonce string to use when generating a Google ID token
                    .setNonce(hashedNonce)
                    .build()*/


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
                Log.d(TAG, "startGoogleAuthentication: res: ${response.credential.data}")
                emit(!response.credential.data.isEmpty)


                BundlePrinter.printBundleContents(response.credential.data)

            } catch (e: NoCredentialException) {
                Toast.makeText(context, "No credential found", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startGoogleAuthentication: ", e)
                emit(false)
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startGoogleAuthentication: ", e)
                emit(false)
            }

        }
    }
}