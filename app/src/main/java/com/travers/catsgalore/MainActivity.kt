package com.travers.catsgalore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.createBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        }

    fun main() {
        //var button_new_cat = findViewById(R.id.button_newcat) as Button
        //var button_new_cat = Button(findViewById(R.id.button_newcat))
        var button_new_cat = findViewById<Button>(R.id.button_newcat)
        // all 3 above are the same

        button_new_cat.setOnClickListener {
            //it.setBackgroundColor(Color.parseColor("#FFFFFF"))
            showNewCat(it)
        }
    }

    fun showNewCat(vue: View) {
        var image_cat_main = findViewById<ImageView>(R.id.image_cat_main)
        //image_cat_main.setImageURI("")
        thread {
            runBlocking { //runCatching?
                launch {
                    val catUrlAPI = URL("https://api.thecatapi.com/v1/images/search")
                    val catResponseJson = catUrlAPI.readText()
                    val typeToken = object: TypeToken<List<Map<String, Any>>>() {}.type
                    val catResponseJsonDeserialized = Gson().fromJson<List<Map<String, Any>>>(catResponseJson, typeToken)[0]
                    // was bugging about <List<String>> since width is integer
                    Log.d(javaClass.simpleName, catResponseJsonDeserialized["url"] as String)//["url"].toString())
                    val catUrlImage = URL(catResponseJsonDeserialized["url"] as String)
                    val catImage = BitmapFactory.decodeStream(catUrlImage.openConnection().getInputStream())
                    //Log.d(javaClass.simpleName, url) //I'm really gonnna have to switch to camelCase to use this language huh
                    runOnUiThread { image_cat_main.setImageBitmap(catImage) }
                }
            }
        }
        //withContext() {}
        //var tview = findViewById<TextView>(R.id.text_main)
        //tview.setText(x) //R.string.text_main2) //"@string/text_main2"
        //vue.setBackgroundColor(Color.parseColor("#ff0000")
    }
}