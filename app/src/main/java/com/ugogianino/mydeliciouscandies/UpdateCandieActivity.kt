package com.ugogianino.mydeliciouscandies

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.ugogianino.mydeliciouscandies.adapters.CandieAdapter
import com.ugogianino.mydeliciouscandies.adapters.MyDeliciousCandiesDBAdapter
import com.ugogianino.mydeliciouscandies.databinding.ActivityUpdateCandieBinding
import com.ugogianino.mydeliciouscandies.model.Candie
import java.io.ByteArrayOutputStream

class UpdateCandieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateCandieBinding
    private lateinit var candieAdapter: CandieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCandieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val name = intent.getStringExtra("name")
        binding.nameEditText.setText(name)

        val manufacturer = intent.getStringExtra("manufacturer")
        binding.manufacturerEditText.setText(manufacturer)

        val candyType = intent.getStringExtra("candyType")
        binding.typeEditText.setText(candyType)

        val format = intent.getStringExtra("format")
        binding.formatEditText.setText(format)

        val sweetness = intent.getIntExtra("sweetness", 0)
        binding.sweetnessEditText.setText(sweetness.toString())

        val image = intent.getByteArrayExtra("image")
        if (image != null) {
            binding.imageView3.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.size))
        }

        val url = intent.getStringExtra("url")
        binding.urlEditText.setText(url)

        val isFavourite = intent.getBooleanExtra("isFavourite", false)
        binding.favoriteCheckbox.isChecked = isFavourite

        binding.updateButton.setOnClickListener {
            val id = intent.getIntExtra("id", 0)
            val name = binding.nameEditText.text.toString()
            val manufacturer = binding.manufacturerEditText.text.toString()
            val candyType = binding.typeEditText.text.toString()
            val format = binding.formatEditText.text.toString()
            val sweetness = binding.sweetnessEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val isFavourite = binding.favoriteCheckbox.isChecked
            val imageView = binding.imageView3
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val image = byteArrayOutputStream.toByteArray()

            if (validateInputs(name, manufacturer, candyType, format, sweetness, url)) {
                val candie = Candie(
                    id = id,
                    name = name,
                    manufacturer = manufacturer,
                    candyType = candyType,
                    saleFormat = format,
                    sweetness = sweetness.toInt(),
                    image = image,
                    url = url,
                    isFavourite = isFavourite
                )
                MyDeliciousCandiesDBAdapter.getInstance(this).updateCandie(candie)
                finish()
            }

        }

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

}