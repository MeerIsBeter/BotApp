package com.example.chatbot

import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


class MainMenuTouchListener(function: () -> (Unit), view: View) : OnTouchListener {
    var dX = 0f
    var dY: Float = 0f
    var lastAction: Int = -10
    val clickAction: () -> Unit = function

    private val rootPosX = view.x
    private val rootPosY = view.y

    private var animationX: SpringAnimation = createSpringAnimation(view, rootPosX, DynamicAnimation.X)
    private var animationY: SpringAnimation = createSpringAnimation(view, rootPosY, DynamicAnimation.Y)

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { // the button is pressed
                // compute the difference between the top-left of the view and where the event occurs
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN

                animationX.cancel()
                animationY.cancel()
            }
            MotionEvent.ACTION_MOVE -> { // the button is moved
                view.y = event.rawY + dY
                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            // perform clickAction() if the button was not moved between press and release
            MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) {
                doBubbleAnimation(view)
                // perform the button-specific action
                clickAction()
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK)
            } else { // bounce back to its original position
                animationX.start()
                animationY.start()
            }
            else -> return false
        }
        return true
    }


    private fun doBubbleAnimation(view: View) {
        val bubbleAnimation: Animation = AnimationUtils.loadAnimation(view.context, R.anim.grow)

        val interpolator = BubbleInterpolator(0.1, 20.0)
        bubbleAnimation.interpolator = interpolator

        view.startAnimation(bubbleAnimation)
    }

    private fun createSpringAnimation(view: View, pos: Float, direction: FloatPropertyCompat<View>): SpringAnimation {
        val animation = SpringAnimation(view, direction)

        val spring = SpringForce()
        spring.finalPosition = pos
        spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        spring.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        animation.spring = spring

        return animation
    }
}