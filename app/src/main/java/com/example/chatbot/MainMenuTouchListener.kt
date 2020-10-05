package com.example.chatbot

import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.startActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlin.concurrent.thread

class MainMenuTouchListener(function: () -> (Unit)) : OnTouchListener {
    var dX = 0f
    var dY: Float = 0f
    var lastAction: Int = -10
    var rootPosX = 0f
    var rootPosY = 0f
    val clickAction: () -> Unit = function

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN
                rootPosX = view.x
                rootPosY = view.y
            }
            MotionEvent.ACTION_MOVE -> {
                view.y = event.rawY + dY
                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) {
                doBubbleAnimation(view)
                // perform the button-specific action
                clickAction()
            } else {
                doSpringAnimation(view, rootPosX, rootPosY)
            }
            else -> return false
        }
        return true
    }

    fun doBubbleAnimation(view: View) {
        val bubbleAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.grow)

        val interpolator = BubbleInterpolator(0.1, 20.0)
        bubbleAnimation.interpolator = interpolator

        view.startAnimation(bubbleAnimation)
    }

    fun doSpringAnimation(view: View, x: Float, y: Float) {
        val animationX: SpringAnimation = SpringAnimation(view, DynamicAnimation.X)

        val springX: SpringForce = SpringForce()
        springX.finalPosition = x
        springX.stiffness = SpringForce.STIFFNESS_MEDIUM
        springX.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        animationX.spring = springX


        val animationY: SpringAnimation = SpringAnimation(view, DynamicAnimation.Y)

        val springY: SpringForce = SpringForce()
        springY.finalPosition = y
        springY.stiffness = SpringForce.STIFFNESS_MEDIUM
        springY.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        animationY.spring = springY

        animationX.start()
        animationY.start()
    }
}