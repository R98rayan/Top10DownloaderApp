package com.example.top10downloaderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlparsingrssfeedhttpurlconnections.XmlParser
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var rvAdapter: RVAdapter

    private lateinit var  button10: Button
    private lateinit var  button100: Button

    private lateinit var frameLayout: FrameLayout

    var list = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.frameLayout)

        button10 = findViewById(R.id.button10)
        button100 = findViewById(R.id.button100)

        rvMain = findViewById(R.id.rvMain)
        rvAdapter = RVAdapter(list)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)

        button10.setOnClickListener{
            parseRSS(10)
//            frameLayout.visibility = View.INVISIBLE
        }
        button100.setOnClickListener{
            parseRSS(100)
//            frameLayout.visibility = View.INVISIBLE
        }

    }

    private fun parseRSS(number: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val data = async {
                val parser = XmlParser()
                parser.parse(number)
            }.await()
            try{
                withContext(Dispatchers.Main){
                    rvAdapter.update(data)
                }
            }catch(e: java.lang.Exception){
                Log.d("MAIN", "Unable to get data")
            }
        }
    }
}