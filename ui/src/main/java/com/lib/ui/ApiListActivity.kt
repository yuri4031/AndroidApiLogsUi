package com.lib.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.db.dto.Api
import com.lib.ui.adapter.ApiListAdapter
import com.lib.ui.databinding.ActivityApiListBinding

class ApiListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiListBinding
    private lateinit var apiListViewModel: ApiListViewModel

    private lateinit var apiListAdapter: ApiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_api_list)

        apiListAdapter =
            ApiListAdapter(arrayListOf(), object : ApiListAdapter.ApiDataListCallback {
                override fun onItemClick(api: Api) {
                    val intent = Intent(this@ApiListActivity, ApiDetailActivity::class.java)
                    intent.putExtra("id", api.apiId)
                    startActivity(intent)
                }

                override fun onChecked(api: Api) {
                }
            })
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = apiListAdapter

        apiListViewModel = ViewModelProvider(this)[ApiListViewModel::class.java]

        apiListViewModel.allApis.observe(this, Observer<List<Api>> { t ->
            if (t.isNullOrEmpty()) {
                Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show()
            } else {
                val controller = AnimationUtils.loadLayoutAnimation(
                    binding.recyclerView.context,
                    R.anim.layout_animation_fall_down
                )

                binding.recyclerView.layoutAnimation = controller
                apiListAdapter.setList(t)
                binding.recyclerView.scheduleLayoutAnimation()

            }
        })

    }

}

