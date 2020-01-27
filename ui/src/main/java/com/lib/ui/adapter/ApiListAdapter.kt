package com.lib.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lib.db.dto.Api
import com.lib.ui.databinding.ItemApiBinding

class ApiListAdapter(
    private var arrayListApiData: ArrayList<Api>,
    val apiDataListCallback: ApiDataListCallback
) : RecyclerView.Adapter<ApiListAdapter.ApiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiViewHolder {
        val binding =
            ItemApiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiViewHolder(binding)
    }

    override fun getItemCount() = arrayListApiData.size

    fun setList(arrayListApiData: List<Api>) {
        this.arrayListApiData = ArrayList(arrayListApiData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ApiViewHolder, position: Int) {
        holder.bind(arrayListApiData.get(position), position)
    }


    inner class ApiViewHolder(private val binding: ItemApiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Api?, position: Int) {
            binding.api = item
            binding.layout.setOnClickListener {
                apiDataListCallback.onItemClick(arrayListApiData[position])
            }
//            binding.checkbox.setOnClickListener {
//                mohListCallback.mohItemAddToCart(arrayListApiData[position])
//            }
        }
    }

    fun getCheckedList(): ArrayList<Api> {
        return ArrayList(arrayListApiData.filter { it.checked })
    }

    interface ApiDataListCallback {
        fun onItemClick(api: Api)
        fun onChecked(api: Api)
    }
}
