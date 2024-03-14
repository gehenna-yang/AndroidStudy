package com.kotlinex.coinproject.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlinex.coinproject.R
import com.kotlinex.coinproject.datamodel.CoinList

class SelectAdapter (val context: Context, val coinlist: List<CoinList>):
    RecyclerView.Adapter<SelectAdapter.ViewHolder>() {

    val selectedCoinList = ArrayList<String>()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val coinName : TextView = view.findViewById(R.id.coinName)
        val coinPriceUpDown : TextView = view.findViewById(R.id.coinPriceUpDown)
        val likeImage : ImageView = view.findViewById(R.id.likeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.intro_coin_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.coinName.text = coinlist[position].coinName

        val fluctate_24H = coinlist[position].coinInfo.fluctate_24H

        if(fluctate_24H.contains("-")){
            holder.coinPriceUpDown.text = "하락입니다."
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#114fed"))
        } else {
            holder.coinPriceUpDown.text = "상승입니다."
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#ed2e11"))
        }

        val likeImg = holder.likeImage
        val cc = coinlist[position].coinName

        if(selectedCoinList.contains(cc)){
            likeImg.setImageResource(R.drawable.like_red)
        } else {
            likeImg.setImageResource(R.drawable.like_grey)
        }

        likeImg.setOnClickListener {
            if(selectedCoinList.contains(cc)) {
                selectedCoinList.remove(cc)
                likeImg.setImageResource(R.drawable.like_grey)
            } else {
                selectedCoinList.add(cc)
                likeImg.setImageResource(R.drawable.like_red)
            }
        }
    }

    override fun getItemCount(): Int {
        return coinlist.size
    }
}