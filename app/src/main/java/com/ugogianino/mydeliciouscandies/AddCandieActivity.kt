package com.ugogianino.mydeliciouscandies


import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ugogianino.mydeliciouscandies.adapters.MyDeliciousCandiesDBAdapter
import com.ugogianino.mydeliciouscandies.databinding.ActivityAddCandieBinding
import com.ugogianino.mydeliciouscandies.model.Candie

class AddCandieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCandieBinding
    private var listaCandie: MutableList<Candie> = mutableListOf()
    private lateinit var recycler: RecyclerView


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

            if (validateInputs(name, manufacturer, candyType, format, sweetness, url)) {
                val candie = Candie(
                    0,
                    name = name,
                    manufacturer = manufacturer,
                    candyType = candyType,
                    saleFormat = format,
                    sweetness = sweetness.toInt(),
                    image = null,
                    url = url,
                    isFavourite = isFavourite
                )
                MyDeliciousCandiesDBAdapter.getInstance(this).addCandie(candie)
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
