package com.ugogianino.mydeliciouscandies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugogianino.mydeliciouscandies.adapters.CandieAdapter
import com.ugogianino.mydeliciouscandies.adapters.MyDeliciousCandiesDBAdapter
import com.ugogianino.mydeliciouscandies.databinding.ActivityMainBinding
import com.ugogianino.mydeliciouscandies.model.Candie

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listaCandie: MutableList<Candie> = mutableListOf()
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaCandie = MyDeliciousCandiesDBAdapter.getInstance(this).getAllCandies() as MutableList<Candie>
        estableceradapatador()

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddCandieActivity::class.java)
            startActivity(intent)
        }
    }

    private fun estableceradapatador(){
        recycler = binding.listaCandies
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CandieAdapter(this, listaCandie)
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        listaCandie = MyDeliciousCandiesDBAdapter.getInstance(this).getAllCandies() as MutableList<Candie>
        estableceradapatador()
    }

}