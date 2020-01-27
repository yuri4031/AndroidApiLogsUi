package com.lib.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lib.db.dto.Api
import com.lib.db.dto.ApiBodyAndHeader
import com.lib.db.dto.ApiHeader
import com.lib.ui.databinding.ActivityApiDetailBinding
import com.lib.ui.databinding.ActivityApiListBinding
import kotlinx.android.synthetic.main.item_header_row.view.*

class ApiDetailActivity : AppCompatActivity() {

    private lateinit var apiDetailViewModel: ApiDetailViewModel
    private lateinit var binding: ActivityApiDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_api_detail)

        val apiId = intent.getLongExtra("id", -1)

        apiDetailViewModel = ViewModelProvider(this)[ApiDetailViewModel::class.java]

        apiDetailViewModel.findById(apiId).observe(this, Observer { t ->
            t?.let {
                binding.api = t.api
                if (t.apiHeaderList.isNullOrEmpty()) {
                    binding.headerLayout.visibility = View.GONE
                } else {
                    t.apiHeaderList?.let { showHeaders(it) }
                }
                binding.bodyJson.bindJson(t.api?.requestBody)
                if (t.api?.hasErrorCode() == true) {
                    binding.responseJson.bindJson(t.api?.responseBody)
                    binding.responseScroll.visibility = View.VISIBLE
                } else {
                    binding.textViewResponseMessage.visibility = View.VISIBLE
                    binding.textViewResponseMessage.text = t.api?.responseBody
                }
            }
        })


    }

    private fun showHeaders(apiHeaderList: List<ApiHeader>) {
        apiHeaderList.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.item_header_row, null, false)
            view.key.text = it.name
            view.value.text = it.value
            binding.tableLayout.addView(view)
        }

    }
}
