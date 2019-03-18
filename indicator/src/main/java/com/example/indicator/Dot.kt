package com.example.indicator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF


class Dot {

    private var paint: Paint? = null

    private var center: PointF? = null

    private var currentRadius: Float = 0.0F

    fun Dot(){
        paint = Paint()
        paint!!.isAntiAlias = true
        center = PointF()
    }

    fun setColor(color: Int) {
        paint!!.setColor(color)
    }

    fun setAlpha(alpha: Int) {
        paint!!.setAlpha(alpha)
    }

    fun setCenter(x: Float, y: Float) {
        center!!.set(x, y)
    }

    fun getCurrentRadius(): Float {
        return currentRadius
    }

    fun setCurrentRadius(radius: Float) {
        this.currentRadius = radius
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(center!!.x, center!!.y, currentRadius, paint!!)
    }
}