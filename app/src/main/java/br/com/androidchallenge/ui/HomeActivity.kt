package br.com.androidchallenge.ui

import android.Manifest
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import br.com.androidchallenge.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.activity_home.*
import permissions.dispatcher.*
import java.util.*

@RuntimePermissions
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        generateQRCode("teste")
        button.setOnClickListener {

            showCameraWithPermissionCheck()

//            val intent = Intent(this, ScannerActivity::class.java)
//            startActivity(intent)

        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        //Método que solicita a permissão e caso ela seja garantida
        Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        //Este método server para exibir uma mensagem ao usuário antes do Dialog que irá pedir a permissão.
        AlertDialog.Builder(this)
            .setPositiveButton(getString(R.string.permission_yes)) { _, _ -> request.proceed() }
            .setNegativeButton(getString(R.string.permission_no)) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(getString(R.string.permission_message))
            .show()
    }


    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun onCameraDenied() {
        //Este método será chamado caso o usuário rejeite a permissão
        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun onCameraNeverAskAgain() {
        //Este método será chamado caso o usuário rejeite a permissão e não queira que o recurso seja solicitado
        Toast.makeText(this, getString(R.string.permission_never_again), Toast.LENGTH_SHORT).show()
    }


    fun generateQRCode(param: String){
        try {

            val width = 300
            val height = 300
            val smallestDimension = if (width < height) width else height

            //setting parameters for qr code
            val charset = "UTF-8" // or "ISO-8859-1"
            val hintMap: MutableMap<EncodeHintType?, ErrorCorrectionLevel?> =
                HashMap()
            hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            CreateQRCode(param, charset, hintMap, smallestDimension, smallestDimension)
        } catch (ex: java.lang.Exception) {
            Log.e("QrGenerate", ex.message!!)
        }
    }

    fun CreateQRCode(
        qrCodeData: String,
        charset: String?,
        hintMap: Map<*, *>?,
        qrCodeheight: Int,
        qrCodewidth: Int
    ) {
        try {
            val matrix = MultiFormatWriter().encode(
                charset,
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap as MutableMap<EncodeHintType, *>?
            )

            val width = matrix.width
            val height = matrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (matrix[x, y]) ResourcesCompat.getColor(
                        resources,
                        R.color.colorB,
                        null
                    ) else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            val overlay =
                BitmapFactory.decodeResource(this.resources, R.drawable.ico_you)

            imageViewBitmap.setImageBitmap(mergeBitmaps(overlay, bitmap))
        } catch (er: Exception) {
            Log.e("QrGenerate", er.message!!)
        }
    }

    fun mergeBitmaps(overlay: Bitmap, bitmap: Bitmap): Bitmap? {
        val height = bitmap.height
        val width = bitmap.width
        val combined = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(combined)
        val canvasWidth = canvas.width
        val canvasHeight = canvas.height
        canvas.drawBitmap(bitmap, Matrix(), null)
        val centreX = (canvasWidth - overlay.width) / 2
        val centreY = (canvasHeight - overlay.height) / 2
        canvas.drawBitmap(overlay, centreX.toFloat(), centreY.toFloat(), null)
        return combined
    }

}