package com.example.indicator

import android.support.v4.view.ViewPager

interface IndicatorInterface {
    fun setViewPager(viewPager: ViewPager)

    fun setAnimateDuration(duration: Long)

    /**
     *
     * @param radius: radius in pixel
     */
    fun setRadiusSelected(radius: Int)

    /**
     *
     * @param radius: radius in pixel
     */
    fun setRadiusUnselected(radius: Int)

    /**
     *
     * @param distance: distance in pixel
     */
    fun setDistanceDot(distance: Int)
}
