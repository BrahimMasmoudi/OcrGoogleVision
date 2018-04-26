package com.example.brahimmasmoudi.ocrgooglevision

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.GONE
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.bookImageView
import kotlinx.android.synthetic.main.activity_main.button_process
import kotlinx.android.synthetic.main.activity_main.ocrResult
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.drawable.espagnol
        );
        bookImageView.setImageBitmap(bitmap);
        button_process.setOnClickListener({
            val textRecognizer = TextRecognizer.Builder(applicationContext).build()
            if (!textRecognizer.isOperational)
                Log.e("ERROR", "Detector dependencies are not yet available")
            else {
                val frame = Frame.Builder().setBitmap(bitmap).build()
                val items = textRecognizer.detect(frame)
                val stringBuilder = StringBuilder()
                for (i in 0 until items.size()) {
                    val item = items.valueAt(i)
                    stringBuilder.append(item.value)
                    stringBuilder.append("\n")
                }
                ocrResult.setText(stringBuilder.toString())
                Log.e("masmoudiBrahim", stringBuilder.toString())
                Log.e("masmoudiBrahimPath", Environment.getExternalStorageDirectory().getAbsolutePath())
             //  saveFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/masmoudiSfax.txt",stringBuilder.toString().toByteArray())
                 wrtieFileOnInternalStorage(stringBuilder.toString(), "masmoudiSfax")
                bookImageView.visibility = GONE
            }
        })

    }
    fun saveFile(filePath: String, data: ByteArray): Boolean {
        Log.e("brahim","Write $filePath on internal storage")
        try {
            val documentFile = File(filePath)
            documentFile.parentFile.mkdirs()
            documentFile.createNewFile()
            FileOutputStream(documentFile).use { it.write(data) }
        } catch (exception: Exception) {
            Log.e("brahim","An error occur when Writing $filePath on internal storage: {size=${data.size}}", exception)
            return false
        }
        return true
    }
    private fun wrtieFileOnInternalStorage(text: String, fileName: String) {
        val completeFileName = "$fileName.txt"

        var fos = openFileOutput(completeFileName, Context.MODE_PRIVATE)
        try {
            fos = openFileOutput(completeFileName, Context.MODE_PRIVATE)
            fos.write(text.toByteArray())
            fos.close()
            Toast.makeText(this, "saved to " + filesDir + "/" + completeFileName, Toast.LENGTH_LONG).show()
            Log.e("brahim", filesDir.toString() + "/" + completeFileName)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


    }
}
