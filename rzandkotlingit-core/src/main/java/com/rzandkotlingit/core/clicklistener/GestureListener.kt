package com.rzandkotlingit.core.clicklistener

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

abstract class GestureListener(): GestureDetector.SimpleOnGestureListener() {
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
            val gestureListener = object: GestureListener() {
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
            val gestureDetector = GestureDetector(context, gestureListener)
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


/*
@SuppressLint("ClickableViewAccessibility")
private fun setListenerOld(context: Context, onClickListener: OnClickListener, view: View) {
    /*val gestureDetector = GestureDetector(context, object: GestureListener() {
        override fun onSingleClick(event: MotionEvent?) {
            onSingleClick(event)
        }

        override fun onDoubleClick(event: MotionEvent?) {
            onDoubleClick(event)
        }
    })*/
    /*val gestureListener = object: OnClickListener {
        override fun onSingleClick(event: MotionEvent?) {
            onSingleClick(event)
        }
        //
        override fun onDoubleClick(event: MotionEvent?) {
            onDoubleClick(event)
        }
    }*/
    val gestureListener = object: GestureListener() {
        override fun onSingleClick(event: MotionEvent?) {
            onSingleClick(event)
        }
        //
        override fun onDoubleClick(event: MotionEvent?) {
            onDoubleClick(event)
        }
    }
    val gestureDetector = GestureDetector(context, gestureListener)
    view.setOnTouchListener { innerView, event -> gestureDetector.onTouchEvent(event); }
}
*/

/*
/*val gestureDetector = GestureDetector(context, object : GestureListener() {
    override fun onSingleClick() {
        onItemClickListener.let {
            onItemClickListener.onItemFinished(itemView, position)
        }
    }

    override fun onDoubleClick() {
        onItemClickListener.let {
            onItemClickListener.onItemDoubleClick(itemView, position)
        }
    }
})
itemView.setOnTouchListener { view, event -> gestureDetector.onTouchEvent(event); }*/
/*GestureListener(object: GestureListener() {
}).setListener(context, itemView)*/
/*var gestureListener = object: GestureListener() {
    override fun onSingleClick(event: MotionEvent?) {
        println("DEBUG_LOG_PRINT: onSingleClick")
    }

    override fun onDoubleClick(event: MotionEvent?) {
        println("DEBUG_LOG_PRINT: onDoubleClick")
    }
}.setListener(context, itemView)*/
*/