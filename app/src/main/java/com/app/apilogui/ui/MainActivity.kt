package com.app.apilogui.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.apilogui.MyApplication
import com.app.apilogui.R
import com.app.apilogui.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.lib.retrofit.log.RetrofitCallback
import com.lib.ui.ApiListActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        context = this

        val gson = Gson()
        val jsonStr = "{\n" +
                "    \"app-platform\": \"Android\",\n" +
                "    \"app-version\": \"3.3.7\",\n" +
                "    \"device-id\": \"4:b1:67:3:f5:bf\",\n" +
                "    \"msisdn\": \"97333008434\"\n" +
                "}"

        val element = gson.fromJson(jsonStr, JsonElement::class.java)
        val jsonObj = element.asJsonObject

        binding.buttonCheck.setOnClickListener {
            MyApplication.getApiService()?.checkUser(jsonObj)
                ?.enqueue(RetrofitCallback(this@MainActivity, object : Callback<JSONObject> {
                    override fun onFailure(call: Call<JSONObject>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<JSONObject>,
                        response: Response<JSONObject>
                    ) {
                    }
                }))
        }

        val jsonStrLogin = "{\n" +
                "    \"app-platform\": \"Android\",\n" +
                "    \"app-version\": \"3.4.0\",\n" +
                "    \"device-id\": \"4:b1:67:3:f5:bf\",\n" +
                "    \"msisdn\": \"97314141414\",\n" +
                "    \"pin\": \"1234\",\n" +
                "    \"app-push-token\": \"dqU_Aov9GHA:APA91bGvRF2auZEMqGIDjo-rIYJbzAoDP5RWmHVSrCap_Yxv_tRoR2VNdQyNLw1xIuIkDX3G27du8H3vCWM0xVrKf533Z-44sjgG28KS1vveSjSOTDNnFXhkKkyP4G8Kvwp2kYyhMDS8\"\n" +
                "}"

        val elementLogin = gson.fromJson(jsonStrLogin, JsonElement::class.java)
        val jsonLogin = elementLogin.asJsonObject

        binding.buttonLogin.setOnClickListener {
            MyApplication.getApiService()?.loginUser(jsonLogin)
                ?.enqueue(RetrofitCallback(this@MainActivity, object : Callback<JSONObject> {
                    override fun onFailure(call: Call<JSONObject>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<JSONObject>,
                        response: Response<JSONObject>
                    ) {
                    }
                }))
        }

        binding.buttonList.setOnClickListener {
            startActivity(Intent(context, ApiListActivity::class.java))
        }

    }
}
