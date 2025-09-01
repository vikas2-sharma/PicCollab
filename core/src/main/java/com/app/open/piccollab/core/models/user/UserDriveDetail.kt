package com.app.open.piccollab.core.models.user

data class UserDriveDetail(
    val user: User?,
    val storageQuota: StorageQuota?
)

data class User(
    val kind: String?,
    val displayName: String?,
    val photoLink: String?,
    val me: String?,
    val permissionId: String?,
    val emailAddress: String?,
)

data class StorageQuota(
    val limit:String?,
    val usage:String?,
    val usageInDrive:String?,
    val usageInDriveTrash:String?,
)