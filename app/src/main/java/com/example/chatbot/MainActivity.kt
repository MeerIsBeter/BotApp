package com.example.chatbot

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Python.start(AndroidPlatform(applicationContext))

        fun setBubbleAnimation(view: View) {
            val bubbleAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.grow)

            val interpolator = BubbleInterpolator(0.1, 20.0)
            bubbleAnimation.interpolator = interpolator

            view.startAnimation(bubbleAnimation)
        }

        val botButton = findViewById<Button>(R.id.bot_button)

        botButton.setOnTouchListener(object: View.OnTouchListener {
            var dX: Float = 0f
            var dY: Float = 0f
            var lastAction: Int = -10

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                    }
                    MotionEvent.ACTION_MOVE -> {
                        view.y = event.rawY + dY
                        view.x = event.rawX + dX
                        lastAction = MotionEvent.ACTION_MOVE
                    }
                    MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) {
                        setBubbleAnimation(botButton)

                        thread(start=true) {
                            Thread.sleep(800)

                            val intent = Intent(view.context, MessageListActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else -> return false
                }
                return true
            }
        })

        val helpButton = findViewById<Button>(R.id.help_button)

        helpButton.setOnTouchListener(object: View.OnTouchListener {
            var dX: Float = 0f
            var dY: Float = 0f
            var lastAction: Int = -10

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                        lastAction = MotionEvent.ACTION_DOWN
                    }
                    MotionEvent.ACTION_MOVE -> {
                        view.y = event.rawY + dY
                        view.x = event.rawX + dX
                        lastAction = MotionEvent.ACTION_MOVE
                    }
                    MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) {
                        setBubbleAnimation(helpButton)

                        // TODO: make help page and redirect to it
                    }
                    else -> return false
                }
                return true
            }
        })
    }
}