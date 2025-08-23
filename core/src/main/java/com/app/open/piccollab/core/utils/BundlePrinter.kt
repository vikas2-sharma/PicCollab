package com.app.open.piccollab.core.utils

import android.os.Bundle
import android.util.Log


object BundlePrinter {
    private const val TAG = "BundlePrinter"

    fun printBundleContents(bundle: Bundle?) {
        if (bundle == null) {
            Log.d(TAG, "Bundle is null.")
            return
        }

        if (bundle.isEmpty) {
            Log.d(TAG, "Bundle is empty.")
            return
        }

        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            if (value is Bundle) {
                Log.d(TAG, "Key: $key (Nested Bundle)")
                printBundleContents(value) // Recursively print nested Bundles
            } else if (value is Array<*> && value.isArrayOf<Any>()) {
                Log.d(TAG, "Key: " + key + " : " + (value as Array<*>).contentToString())
            } else {
                Log.d(TAG, "Key: $key : $value")
            }
        }
    }
}