package com.rzandkotlingit.core.clicklistener

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

abstract class DoubleClickListener: GestureDetector.SimpleOnGestureListener() {
    private lateinit var onClickListenerInner: OnClickListenerInner
    init {
    }
    override fun onDown(event: MotionEvent?): Boolean {
        //        println("DEBUG_LOG_PRINT: onDown")
        return true
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        //        println("DEBUG_LOG_PRINT: onSingleTapUp")
        return true
    }
    //
    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        //        println("DEBUG_LOG_PRINT: onSingleTapConfirmed")
        onSingleClick(event)
        return true
    }
    //
    override fun onDoubleTap(event: MotionEvent?): Boolean {
        //        println("DEBUG_LOG_PRINT: onDoubleTap")
        onDoubleClick(event)
        return true
    }
    //
    companion object {
        @SuppressLint("ClickableViewAccessibility")
        @JvmStatic
        fun setEventListener(context: Context, view: View, onClickListener: OnClickListener) {
            val doubleClickListener = object: DoubleClickListener() {
                override fun onSingleClick(event: MotionEvent?) {
                    onClickListener.let {
                        onClickListener.onSingleClick(event)
                    }
                }
                //
                override fun onDoubleClick(event: MotionEvent?) {
                    onClickListener.let {
                        onClickListener.onDoubleClick(event)
                    }
                }
            }
            val gestureDetector = GestureDetector(context, doubleClickListener)
            view.setOnTouchListener { innerView, event -> gestureDetector.onTouchEvent(event); }
        }
    }
    //
    internal interface OnClickListenerInner {
        fun onSingleClick(event: MotionEvent?)
        fun onDoubleClick(event: MotionEvent?)
    }
    // Abstract function for implementation
    abstract fun onSingleClick(event: MotionEvent?)
    abstract fun onDoubleClick(event: MotionEvent?)
    public interface OnClickListener {
        fun onSingleClick(event: MotionEvent?)
        fun onDoubleClick(event: MotionEvent?)
    }
}