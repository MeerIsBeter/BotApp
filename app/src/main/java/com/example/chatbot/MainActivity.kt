package com.example.chatbot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.DynamicAnimation
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

        botButton.setOnTouchListener(MainMenuTouchListener {
            thread(start = true) {
                Thread.sleep(800)

                val intent = Intent(botButton.context, MessageListActivity::class.java)
                startActivity(intent)
            }
        })

        val helpButton = findViewById<Button>(R.id.help_button)

        helpButton.setOnTouchListener(MainMenuTouchListener {
            println("Nothing yet!")
        })
    }
}