package com.ugogianino.mydeliciouscandies

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugogianino.mydeliciouscandies.adapters.CandieAdapter
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
            val sweetness = binding.sweetnessEditText.text.toString().toInt()
            //val image = binding.imageEditText.text.toString()
            val url = binding.urlEditText.text.toString()
            val isFavourite =binding.favoriteCheckbox.isChecked

            val candie = Candie(0, name = name, manufacturer = manufacturer, candyType = candyType,
                saleFormat = format, sweetness = sweetness, image = null, url = url, isFavourite = isFavourite
            )
            MyDeliciousCandiesDBAdapter.getInstance(this).addCandie(candie)
            finish()
        }
    }


}
