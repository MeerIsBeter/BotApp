package com.example.chatbot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Python.start(AndroidPlatform(applicationContext))

        fun setBubbleAnimation(view: View) {
            val bubbleAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.grow)

            val interpolator: BubbleInterpolator = BubbleInterpolator(0.1, 20.0)
            bubbleAnimation.interpolator = interpolator

            view.startAnimation(bubbleAnimation)
        }

        val botButton = findViewById<Button>(R.id.bot_button)
        botButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                setBubbleAnimation(botButton)

                thread(start=true) {
                    Thread.sleep(800)

                    val intent = Intent(view.context, MessageListActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        val helpButton = findViewById<Button>(R.id.help_button)
        helpButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                setBubbleAnimation(helpButton)

//                thread(start=true) {
//                    Thread.sleep(800)
//
//                    val intent = Intent(view.context, MessageListActivity::class.java)
//                    startActivity(intent)
//                }
            }
        })


//        button.setOnLongClickListener(object: View.OnLongClickListener {
//            override fun onLongClick(view: View): Boolean {
//                val shrinkAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.shrink)
//
//                val interpolator: BubbleInterpolator = BubbleInterpolator(0.05, 20.0)
//                shrinkAnimation.interpolator = interpolator
//
//                button.startAnimation(shrinkAnimation)
//                return true
//
////                val intent = Intent(view.context, MessageListActivity::class.java)
////                startActivity(intent)
//            }
//        })

    }
}