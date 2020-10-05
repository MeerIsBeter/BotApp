package com.example.chatbot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Python.start(AndroidPlatform(applicationContext))

        val botButton = findViewById<Button>(R.id.bot_button)
        val helpButton = findViewById<Button>(R.id.help_button)

        // only create the touch listeners once the global layout has been loaded,
        // to ensure the correct button coordinates are passed
        val globalLayoutListener = OnGlobalLayoutListener {
            botButton.setOnTouchListener(MainMenuTouchListener ( {
                // start a new thread to start MessageListActivity
                thread(start = true) {
                    Thread.sleep(500)

                    val intent = Intent(botButton.context, MessageListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                }
            }, botButton.x, botButton.y))

            helpButton.setOnTouchListener(MainMenuTouchListener ( {
                println("Nothing yet!")
            }, helpButton.x, helpButton.y))
        }

        botButton.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}