package com.ugogianino.mydeliciouscandies.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ugogianino.mydeliciouscandies.R
import com.ugogianino.mydeliciouscandies.UpdateCandieActivity
import com.ugogianino.mydeliciouscandies.model.Candie


class CandieAdapter(var context: Context, var listCandie: MutableList<Candie>):RecyclerView.Adapter<CandieAdapter.ViewHolder>() {
    private val dbAdapter = MyDeliciousCandiesDBAdapter(context)
    private var deletedItem: Candie? = null
    private var candie: List<Candie> = emptyList()
    private var mLastClickTime: Long = -1




    @SuppressLint("CutPasteId")
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        lateinit var labelName: TextView
        lateinit var labelType: TextView
        lateinit var labelFormat: TextView
        lateinit var labelManufacturer: TextView
        lateinit var labelCandyType: TextView
        lateinit var labelSweetness: TextView
        lateinit var labelUrl: TextView
        lateinit var image: ImageView


        init {
            labelName = itemView.findViewById(R.id.txtName)
            labelType = itemView.findViewById(R.id.txtType)
            labelFormat = itemView.findViewById(R.id.txtFormat)
            labelManufacturer = itemView.findViewById(R.id.txtManufactured)
            labelCandyType = itemView.findViewById(R.id.txtType)
            labelSweetness = itemView.findViewById(R.id.txtSweetness)
            labelUrl = itemView.findViewById(R.id.txtUrl)
            image = itemView.findViewById(R.id.imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.card_candie, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return listCandie.size
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val candies = listCandie[position]

        holder.labelName.text = candies.name
        holder.labelType.text = candies.candyType
        holder.labelFormat.text = candies.saleFormat
        holder.labelManufacturer.text = candies.manufacturer
        holder.labelCandyType.text = candies.candyType
        holder.labelSweetness.text = candies.sweetness.toString()
        holder.labelUrl.text = candies.url
        holder.image.setImageBitmap(candies.image?.let { BitmapFactory.decodeByteArray(candies.image, 0, it.size) })

        holder.itemView.setOnLongClickListener {
            deletedItem = candies
            dbAdapter.deleteCandie(candies.id)
            listCandie.removeAt(position)
            notifyItemRemoved(position)
            Snackbar.make(holder.itemView, "Item deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    if (deletedItem != null) {
                        dbAdapter.addCandie(deletedItem!!)
                        listCandie.add(position, deletedItem!!)
                        //notifyItemRangeChanged(position, 1)
                        notifyItemInserted(position)
                        //notifyDataSetChanged()
                    }
                }.show()
            true
        }

        holder.itemView.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                val intent = Intent(holder.itemView.context, UpdateCandieActivity::class.java)
                intent.putExtra("id", candies.id)
                intent.putExtra("name", candies.name)
                intent.putExtra("manufacturer", candies.manufacturer)
                intent.putExtra("candyType", candies.candyType)
                intent.putExtra("format", candies.saleFormat)
                intent.putExtra("sweetness", candies.sweetness)
                intent.putExtra("image", candies.image)
                intent.putExtra("url", candies.url)
                intent.putExtra("isFavourite", candies.isFavourite)
                holder.itemView.context.startActivity(intent)
            }
            mLastClickTime = SystemClock.elapsedRealtime()
        }
    }

    fun updateItem(position: Int, updatedItem: Candie) {
        listCandie[position] = updatedItem
        notifyItemChanged(position)
    }



}