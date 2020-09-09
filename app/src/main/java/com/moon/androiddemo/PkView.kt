package com.moon.androiddemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.pkview.view.*


internal class PkView : FrameLayout {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.pkview, null)
        addView(view)
        val leftView =
            view.findViewById<ImageView>(R.id.leftIconView)
        val rightView =
            view.findViewById<ImageView>(R.id.rightIconView)
        val btn = view.findViewById<Button>(R.id.btn)

        // 左边
        val leftTopScreenOpen = ValueAnimator.ofFloat(0.5f, 0f).apply {
            duration = 1000
            addUpdateListener {
                //动态更新view的高度
                (pk1Top.layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentHeight = it.animatedValue as Float
                pk1Top.requestLayout()
            }
        }

        val leftBottomScreenOpen = ValueAnimator.ofFloat(0.5f, 0f).apply {
            duration = 1000
            addUpdateListener {
                //动态更新view的高度
                (pk1Bottom.layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentHeight = it.animatedValue as Float
                pk1Bottom.requestLayout()
            }
        }

        val leftTextAni = ObjectAnimator.ofFloat(pk1text, "alpha", 1f, 0f).apply {
            duration = 1000
        }

        // 右边
        val rightTopScreenOpen = ValueAnimator.ofFloat(0.5f, 0f).apply {
            duration = 1000
            addUpdateListener {
                //动态更新view的高度
                (pk2Top.layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentHeight = it.animatedValue as Float
                pk2Top.requestLayout()
            }
        }

        val rightBottomScreenOpen = ValueAnimator.ofFloat(0.5f ,0f).apply {
            duration = 1000
            addUpdateListener {
                //动态更新view的高度
                (pk2Bottom.layoutParams as ConstraintLayout.LayoutParams).matchConstraintPercentHeight = it.animatedValue as Float
                pk2Bottom.requestLayout()
            }
        }

        val rightTextAni = ObjectAnimator.ofFloat(pk2text, "alpha", 1f, 0f).apply {
            duration = 1000
        }


        // 飞翔动画 -----------------
        val leftAlpha = ObjectAnimator.ofFloat(leftView, "alpha", 0f, 1f).apply {
            duration = 1000
        }
        val rightAlpha = ObjectAnimator.ofFloat(rightView, "alpha", 0f, 1f).apply {
            duration = 1000
        }

        val leftY = ObjectAnimator.ofFloat(leftView, "translationY", 0f, -500f).apply {
            duration = 500
        }
        val leftX = ObjectAnimator.ofFloat(leftView, "translationX", 0f, -220f).apply {
            duration = 500
        }

        val rightY = ObjectAnimator.ofFloat(rightView, "translationY", 0f, -500f).apply {
            duration = 500
        }

        val rightX = ObjectAnimator.ofFloat(rightView, "translationX", 0f, 220f).apply {
            duration = 500
        }

        val leftEndAlpha = ObjectAnimator.ofFloat(leftView, "alpha", 1f, 0f).apply {
            duration = 500
        }

        val rightEndAlpha = ObjectAnimator.ofFloat(rightView, "alpha", 1f, 0f).apply {
            duration = 500
        }

        // 左边的头像飞到指定位置后缩放
        val leftEndScaleX = ObjectAnimator.ofFloat(leftView, "scaleX", 1f,1.1f).apply {
            duration = 500
        }
        val leftEndScaleY = ObjectAnimator.ofFloat(leftView, "scaleY", 1f, 1.1f).apply {
            duration = 500
        }

        // 右边的头像飞到指定位置后缩放
        val rightEndScaleX = ObjectAnimator.ofFloat(rightView, "scaleX", 1f,1.1f).apply {
            duration = 500
        }
        val rightEndScaleY = ObjectAnimator.ofFloat(rightView, "scaleY", 1f, 1.1f).apply {
            duration = 500
        }

        btn.setOnClickListener { v: View? ->

            // left
            AnimatorSet().apply {
                play(leftX).with(leftY).after(leftAlpha).before(leftEndAlpha).with(leftEndScaleX).with(leftEndScaleY)

                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        leftTopScreenOpen.start()
                        leftBottomScreenOpen.start()

                        leftTextAni.start()
                    }
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })

                start()
            }

            // right
            AnimatorSet().apply {
                play(rightY).with(rightX).after(rightAlpha).before(rightEndAlpha).with(rightEndScaleX).with(rightEndScaleY)
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        rightTopScreenOpen.start()
                        rightBottomScreenOpen.start()

                        rightTextAni.start()
                    }
                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                start()
            }
        }
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}