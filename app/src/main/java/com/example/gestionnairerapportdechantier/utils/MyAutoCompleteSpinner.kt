package com.example.gestionnairerapportdechantier.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import timber.log.Timber


class MyAutoCompleteSpinner : MaterialAutoCompleteTextView {

    constructor(context: Context) : this(context, null)

    constructor(arg0: Context, arg1: AttributeSet?) : super(arg0, arg1)

    constructor(arg0: Context, arg1: AttributeSet, arg2: Int) : super(arg0, arg1, arg2)

    init {
        isCursorVisible = false
        setEnableSpinner(false)

        setTextColor(Color.BLACK)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && filter != null) {
            performFiltering(null, 0)
        }
        setEnableSpinner(false)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d(event?.action.toString())
        when {
            event?.action == MotionEvent.ACTION_MOVE -> {
                setEnableSpinner(true)
            }
            event?.action == MotionEvent.ACTION_UP -> {
                setEnableSpinner(true)
            }
            event?.action == MotionEvent.ACTION_DOWN -> {
                setEnableSpinner(true)
            }
        }

        if(event?.action == MotionEvent.ACTION_UP) {
            if(event.rawX <= totalPaddingLeft) {
                setEnableSpinner(true)
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    fun setEnableSpinner(enable: Boolean){
        this.isEnabled = enable

    }

    override fun performFiltering(text: CharSequence?, keyCode: Int) {
        super.performFiltering(null, keyCode)
    }
}