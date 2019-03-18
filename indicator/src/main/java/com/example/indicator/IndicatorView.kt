package com.example.indicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View

class IndicatorView : View, IndicatorInterface, ViewPager.OnPageChangeListener {

    private var viewPager: ViewPager? = null

    private var dots: Array<Dot>? = null

    private var animateDuration = DEFAULT_ANIMATE_DURATION

    private var radiusSelected = DEFAULT_RADIUS_SELECTED

    private var radiusUnselected = DEFAULT_RADIUS_UNSELECTED

    private var distance = DEFAULT_DISTANCE

    private var colorSelected: Int = 0

    private var colorUnselected: Int=0

    private var currentPosition: Int = 0

    private var beforePosition: Int = 0

    private var animatorZoomIn: ValueAnimator? = null

    private var animatorZoomOut: ValueAnimator? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView)

        this.radiusSelected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_hado_radius_selected, DEFAULT_RADIUS_SELECTED)

        this.radiusUnselected = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_hado_radius_unselected, DEFAULT_RADIUS_UNSELECTED)

        this.distance = typedArray.getInt(R.styleable.IndicatorView_hado_distance, DEFAULT_DISTANCE)

        this.colorSelected = typedArray.getColor(R.styleable.IndicatorView_hado_color_selected, Color.parseColor("#ffffff"))

        this.colorUnselected = typedArray.getColor(R.styleable.IndicatorView_hado_color_unselected, Color.parseColor("#ffffff"))

        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val yCenter = (height / 2).toFloat()

        val d = distance + 2 * radiusUnselected

        val firstXCenter = (width / 2 - (dots!!.size - 1) * d / 2).toFloat()

        for (i in dots!!.indices) {
            dots!![i].setCenter(if (i == 0) firstXCenter else firstXCenter + d * i, yCenter)
            dots!![i].setCurrentRadius((if (i == currentPosition) radiusSelected else radiusUnselected).toFloat())
            dots!![i].setColor(if (i == currentPosition) colorSelected else colorUnselected)
            dots!![i].setAlpha(if (i == currentPosition) 255 else radiusUnselected * 255 / radiusSelected)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = 2 * radiusSelected

        val width: Int
        val height: Int

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = widthSize
        } else {
            width = 0
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize)
        } else {
            height = desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        for (dot in dots!!) {
            dot.draw(canvas)
        }
    }

    override fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(this)
    }

    private fun initDot(count: Int) {
        if (count < 2)
            dots = Array(count,Dot())
        for (i in dots!!.indices) {
            dots!![i] = Dot()
        }
    }

    override fun setAnimateDuration(duration: Long) {
        this.animateDuration = duration
    }

    override fun setRadiusSelected(radius: Int) {
        this.radiusSelected = radius
    }

    override fun setRadiusUnselected(radius: Int) {
        this.radiusUnselected = radius
    }

    override fun setDistanceDot(distance: Int) {
        this.distance = distance
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        beforePosition = currentPosition
        currentPosition = position

        if (beforePosition == currentPosition) {
            beforePosition = currentPosition + 1
        }

        val animatorSet = AnimatorSet()
        animatorSet.duration = animateDuration

        animatorZoomIn = ValueAnimator.ofInt(radiusUnselected, radiusSelected)
        animatorZoomIn!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            internal var positionPerform = currentPosition

            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val newRadius = valueAnimator.animatedValue as Int
                changeNewRadius(positionPerform, newRadius)
            }
        })

        animatorZoomOut = ValueAnimator.ofInt(radiusSelected, radiusUnselected)
        animatorZoomOut!!.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            internal var positionPerform = beforePosition

            override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                val newRadius = valueAnimator.animatedValue as Int
                changeNewRadius(positionPerform, newRadius)
            }
        })

        animatorSet.play(animatorZoomIn).with(animatorZoomOut)
        animatorSet.start()

    }

    private fun changeNewRadius(positionPerform: Int, newRadius: Int) {
        if (dots!![positionPerform].getCurrentRadius() != newRadius.toFloat()) {
            dots!![positionPerform].setCurrentRadius(newRadius.toFloat())
            dots!![positionPerform].setAlpha(newRadius * 255 / radiusSelected)
            invalidate()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    companion object {

        private val DEFAULT_ANIMATE_DURATION: Long = 200

        private val DEFAULT_RADIUS_SELECTED = 20

        private val DEFAULT_RADIUS_UNSELECTED = 15

        private val DEFAULT_DISTANCE = 40
    }
}