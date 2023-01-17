package com.ugogianino.mydeliciouscandies


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ugogianino.mydeliciouscandies.adapters.MyDeliciousCandiesDBAdapter
import com.ugogianino.mydeliciouscandies.databinding.ActivityAddCandieBinding
import com.ugogianino.mydeliciouscandies.model.Candie
import com.ugogianino.mydeliciouscandies.utils.Utilidades
import java.io.ByteArrayOutputStream


class AddCandieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCandieBinding
    private var listaCandie: MutableList<Candie> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private val CAMERA_REQUEST_CODE = 0
    private val GALLERY_REQUEST_CODE = 1

    private lateinit var utilidades: Utilidades




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCandieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val manufacturer = binding.manufacturerEditText.text.toString()
            val candyType = binding.typeEditText.text.toString()
            val format = binding.formatEditText.text.toString()
            val sweetness = binding.sweetnessEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val isFavourite = binding.favoriteCheckbox.isChecked
            val imageView = binding.imageView3


            val drawable = imageView.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val image = byteArrayOutputStream.toByteArray()

                if (validateInputs(name, manufacturer, candyType, format, sweetness, url)) {
                    val candie = Candie(
                        0,
                        name = name,
                        manufacturer = manufacturer,
                        candyType = candyType,
                        saleFormat = format,
                        sweetness = sweetness.toInt(),
                        image = image,
                        url = url,
                        isFavourite = isFavourite
                    )
                    MyDeliciousCandiesDBAdapter.getInstance(this).addCandie(candie)
                    finish()
                }
            } else {
                Toast.makeText(this, "The image is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }


        binding.addPhoto.setOnClickListener {
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Add Photo!")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> takePhotoFromCamera()
                    options[item] == "Choose from Gallery" -> choosePhotoFromGallery()
                    options[item] == "Cancel" -> dialog.dismiss()
                }
            }
            builder.show()
        }



    }


    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }


    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }



    private fun validateInputs(name: String, manufacturer: String, candyType: String, format: String, sweetness: String, url: String): Boolean {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(manufacturer) || TextUtils.isEmpty(candyType) || TextUtils.isEmpty(format) || TextUtils.isEmpty(sweetness) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        val sweetnessRegex = "^[1-5]$".toRegex()
        if (!sweetnessRegex.matches(sweetness)) {
            Toast.makeText(this, "Sweetness must be a number between 1 and 5", Toast.LENGTH_SHORT).show()
            return false
        }

        val urlRegex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?\$".toRegex()
        if (!urlRegex.matches(url)) {
            Toast.makeText(this, "Invalid url", Toast.LENGTH_SHORT).show()
            return false
        }


        return true

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    binding.imageView3.setImageBitmap(photo)
                }
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data?.data
                    binding.imageView3.setImageURI(selectedImage)
                }
            }
        }
    }
}
