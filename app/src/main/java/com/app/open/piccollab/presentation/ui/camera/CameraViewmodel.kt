package com.app.open.piccollab.presentation.ui.camera

import android.content.Context
import android.util.Log
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executor
import java.util.concurrent.Executors


private const val TAG = "CameraViewmodel"

/*@HiltViewModel*/
@ExperimentalGetImage
class CameraViewmodel : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> get() = _surfaceRequest

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    /**
     * thread to execute image analysis from camera
     */
    private val barcodeThreadExecutor: Executor = Executors.newSingleThreadExecutor()

    /**
     * create barcode scanning option to scan only QR codes
     */
    private val barcodeOptions = BarcodeScannerOptions.Builder().setBarcodeFormats(
        Barcode.FORMAT_QR_CODE
    ).build()

    /**
     * barcode scanning client to process image to extract barcode values
     */
    private val barcodeScanner = BarcodeScanning.getClient(barcodeOptions)

    /**
     * ImageAnalysis use-case to continuously take the image and extract the qr code value
     */
    private val barcodeScannerUserCase = ImageAnalysis.Builder().build().apply {
        targetRotation = Surface.ROTATION_0
        setAnalyzer(
            barcodeThreadExecutor
        ) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )
                barcodeScanner.process(image)
                    .addOnSuccessListener { barcodeList ->
                        if (barcodeList.isNotEmpty()) {
                            Log.d(TAG, "analyze: barcodeSize ${barcodeList.size}")
                            Log.d(TAG, "analyze: barcode value: ${barcodeList[0].rawValue}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            TAG,
                            "analyze: scanning failed",
                            exception
                        )
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }
    }

    suspend fun bindToCamera(applicationContext: Context, lifecycleOwner: LifecycleOwner) {
        val cameraProvider = ProcessCameraProvider.awaitInstance(applicationContext)
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            barcodeScannerUserCase
        )
        try {
            awaitCancellation()
        } finally {
            cameraProvider.unbindAll()
        }
    }
}