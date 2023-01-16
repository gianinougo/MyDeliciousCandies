package com.ugogianino.mydeliciouscandies.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ugogianino.mydeliciouscandies.R
import com.ugogianino.mydeliciouscandies.model.Candie


class CandieAdapter(var context: Context, var listCandie: MutableList<Candie>):RecyclerView.Adapter<CandieAdapter.ViewHolder>() {
    private val dbAdapter = MyDeliciousCandiesDBAdapter(context)
    private var deletedItem: Candie? = null
    private var candie: List<Candie> = emptyList()



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
        var candies = listCandie[position]
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
                        notifyItemRangeChanged(position, 1)
                        //notifyItemInserted(position)
                        //notifyDataSetChanged()
                    }
                }.show()
            true
        }

    }
}