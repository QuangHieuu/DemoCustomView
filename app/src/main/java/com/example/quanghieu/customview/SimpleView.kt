package com.example.quanghieu.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SimpleView(context: Context, attrs: AttributeSet, defStyleAttr: Int) : View(context, attrs,
    defStyleAttr) {

  private var mCirclePaint: Paint? = null
  private var mEyeAndMouthPaint: Paint? = null

  private var mCenterX: Float = 0.toFloat()
  private var mCenterY: Float = 0.toFloat()
  private var mRadius: Float = 0.toFloat()
  private val mArcBounds = RectF()

  init {
    initPaints()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val w = View.MeasureSpec.getSize(widthMeasureSpec)
    val h = View.MeasureSpec.getSize(heightMeasureSpec)

    val size = Math.min(w, h)


    mCenterX = w / 2f
    mCenterY = h / 2f
    mRadius = Math.min(w, h) / 2f

    setMeasuredDimension(size, size)
  }

  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint!!)

    val eyeRadius = mRadius / 5f
    val eyeOffsetX = mRadius / 3f
    val eyeOffsetY = mRadius / 3f
    canvas.drawCircle(mCenterX - eyeOffsetX, mCenterY - eyeOffsetY, eyeRadius,
        mEyeAndMouthPaint!!)
    canvas.drawCircle(mCenterX + eyeOffsetX, mCenterY - eyeOffsetY, eyeRadius,
        mEyeAndMouthPaint!!)

    val mouthInset = mRadius / 3f
    mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset)
    canvas.drawArc(mArcBounds, 45f, 45f, false, mEyeAndMouthPaint!!)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    mCenterX = w / 2f
    mCenterY = h / 2f
    mRadius = Math.min(w, h) / 2f
  }

  private fun initPaints() {
    mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    mCirclePaint!!.style = Paint.Style.FILL
    mCirclePaint!!.color = Color.YELLOW
    mEyeAndMouthPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    mEyeAndMouthPaint!!.style = Paint.Style.STROKE
    mEyeAndMouthPaint!!.strokeWidth = 16 * resources.displayMetrics.density
    mEyeAndMouthPaint!!.strokeCap = Paint.Cap.ROUND
    mEyeAndMouthPaint!!.color = Color.BLACK
  }
}
