package ru.devsp.app.mtgcollections.view.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.widget.*
import ru.devsp.app.mtgcollections.R
import android.util.TypedValue
import android.view.Gravity


class NumberCounterView : LinearLayout {

    private lateinit var counter: TextView
    private lateinit var plus: ImageButton
    private lateinit var minus: ImageButton
    private var clickListener: OnCounterClickListener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    interface OnCounterClickListener {
        fun click(inc: Boolean)
    }

    private fun init(attrs: AttributeSet?) {

        val a = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.NumberCounterView,
                0, 0)

        plus = ImageButton(context)
        minus = ImageButton(context)
        counter = TextView(context)

        val plusIcon = a.getDrawable(R.styleable.NumberCounterView_icon_plus)
        val minusIcon = a.getDrawable(R.styleable.NumberCounterView_icon_minus)
        val defaultCount = a.getString(R.styleable.NumberCounterView_default_count)
        val size = a.getDimensionPixelSize(R.styleable.NumberCounterView_size, 48)
        val counterSize = a.getDimensionPixelSize(R.styleable.NumberCounterView_counter_size, 48)
        val textSize = a.getDimension(R.styleable.NumberCounterView_text_size, 18f) /
                resources.displayMetrics.scaledDensity

        a.recycle()

        val color = ContextCompat.getColor(context, R.color.colorTextMain)

        prepareImageButton(plus, plusIcon, color, size)
        prepareImageButton(minus, minusIcon, color, size)

        counter.text = defaultCount
        counter.layoutParams = LayoutParams(counterSize, LayoutParams.WRAP_CONTENT)
        counter.gravity = Gravity.CENTER
        counter.textSize = textSize

        minus.setOnClickListener({ _ ->
            clickListener?.click(false)
        })

        plus.setOnClickListener({ _ ->
            clickListener?.click(true)
        })

        addView(minus)
        addView(counter)
        addView(plus)
    }


    private fun prepareImageButton(view: ImageButton, drawableResource: Drawable, tintColor: Int, size: Int) {
        DrawableCompat.setTint(drawableResource, tintColor)
        view.setImageDrawable(drawableResource)
        view.layoutParams = LayoutParams(size, size)
        view.scaleType = ImageView.ScaleType.CENTER_CROP

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        view.background = resources.getDrawable(outValue.resourceId, context.theme)
    }

    fun getCount(): String {
        return counter.text.toString()
    }

    fun setCount(count: String) {
        counter.text = count
    }

    fun setOnCounterClickListener(listener: OnCounterClickListener) {
        clickListener = listener
    }
}