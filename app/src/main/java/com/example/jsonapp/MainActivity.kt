package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.net.URL
import kotlin.time.measureTimedValue

class MainActivity : AppCompatActivity() {
    private lateinit var tvadvice:TextView
    private lateinit var btdvice:Button

    val Url = "https://api.adviceslip.com/advice"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvadvice = findViewById(R.id.tvadvice)
        btdvice = findViewById(R.id.btdvice)

        btdvice.setOnClickListener(){

            getApi()

        }






    }

    private fun getApi()
    {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchAdvice()

            }.await()

            if (data.isNotEmpty())
            {

                updateAdvice(data)
            }

        }

    }

    private fun fetchAdvice():String{

        var response=""
        try {
            response = URL(Url).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }

    private suspend fun updateAdvice(data:String)
    {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            tvadvice.text = advice

        }

    }

}