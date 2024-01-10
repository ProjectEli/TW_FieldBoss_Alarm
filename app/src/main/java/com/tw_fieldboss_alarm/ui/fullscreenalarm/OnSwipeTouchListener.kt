package com.tw_fieldboss_alarm.ui.fullscreenalarm

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSwipeTouchListener(context: Context) : View.OnTouchListener {
    private val  gestureDetector: GestureDetector

    companion object {
        private const val swipeXThresholds = 50
        private const val swipeYThresholds = 20
    }

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        try {
            return event?.let { gestureDetector.onTouchEvent(it) }!!
        } catch (e: Exception) {
            // Error 핸들링
        }
        return false
    }

    private inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return super.onDown(e)

        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY: Float = e2.y - e1!!.y
                val diffX: Float = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > swipeXThresholds && abs(velocityX) > swipeYThresholds) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > swipeXThresholds && abs(velocityY) > swipeYThresholds) {
                    if (diffY >0 ) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
            //return super.onFling(e1, e2, velocityX, velocityY)
        }
    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}