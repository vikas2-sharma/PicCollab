package com.app.open.piccollab.core.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object ImageUtil {
    fun getQrBitmap(data: String, size: Int): Bitmap {
        val barcodeEncoder = BarcodeEncoder()

        val encodeBitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 2000, 2000)
        return encodeBitmap
    }
}