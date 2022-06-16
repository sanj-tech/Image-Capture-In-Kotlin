package com.example.imagecapture

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    val REQUEST_IMAGE_GALLERY = 11
    val REQUEST_IMAGE_CAMERA = 12
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imagee)
        imageView.setOnClickListener({
            val builder = AlertDialog.Builder(this)
            builder.setTitle("select Image")
            builder.setMessage("Choose your option")
            builder.setPositiveButton("Gallery") { dialog, which ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"

                startActivityForResult(intent, REQUEST_IMAGE_GALLERY)

            }
            builder.setNegativeButton("Camera") { dialog, which ->
                dialog.dismiss()
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePicture ->
                    takePicture.resolveActivity(packageManager)?.also {
                        val permission = ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CAMERA
                        )
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(android.Manifest.permission.CAMERA),
                                1
                            )


                        } else {
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAMERA)
                        }
                    }
                }

            }
            var dialog: AlertDialog = builder.create()
            dialog.show()


        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            imageView.setImageURI(data.data)
        }
        else
            if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
                imageView.setImageBitmap(data.extras?.get("data") as Bitmap)
            }
            else {
                Toast.makeText(this, "data not found", Toast.LENGTH_LONG).show()
            }





    }
}