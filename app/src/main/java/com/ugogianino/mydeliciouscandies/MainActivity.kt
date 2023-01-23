package com.ugogianino.mydeliciouscandies

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
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
    private lateinit var candieAdapter: CandieAdapter
    private lateinit var adapter: MyDeliciousCandiesDBAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MyDeliciousCandiesDBAdapter.getInstance(this)
        adapter.insertInitialData()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        listaCandie = MyDeliciousCandiesDBAdapter.getInstance(this).getAllCandies() as MutableList<Candie>
        estableceradapatador()
    }



}