package com.colisa.findme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.floor
import kotlin.random.Random

class SpotLightImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var paint = Paint()
    private var shouldDrawSpotLight = false
    private var gameOver = false

    private lateinit var winnerRect: RectF
    private var androidBitmapX = 0f
    private var androidBitmapY = 0f

    private val bitmapAndroid = BitmapFactory.decodeResource(resources, R.drawable.android)
    private val spotlight = BitmapFactory.decodeResource(resources, R.drawable.mask)
    private var shader: Shader
    private val shaderMatrix = Matrix()

    init {
        val bitmap = Bitmap.createBitmap(spotlight.width, spotlight.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        shaderPaint.color = Color.BLACK
        canvas.drawRect(
            0f,
            0f,
            spotlight.width.toFloat(),
            spotlight.height.toFloat(),
            shaderPaint
        )
        // Destination pixels that are not covered by the source pixels are kept
        shaderPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(spotlight, 0f, 0f, shaderPaint)
        shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
    }

    private fun setupWinnerRect() {
        Log.d("SpotLight", "Set new winner rect")
        androidBitmapX = floor(Random.nextFloat() * (width - bitmapAndroid.width))
        androidBitmapY = floor(Random.nextFloat() * (height - bitmapAndroid.height))

        winnerRect = RectF(
            (androidBitmapX),
            (androidBitmapY),
            (androidBitmapX + bitmapAndroid.width),
            (androidBitmapY + bitmapAndroid.height)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupWinnerRect()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmapAndroid, androidBitmapX, androidBitmapY, paint)
        if (!gameOver) {
            if (shouldDrawSpotLight) {
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            } else {
                canvas.drawColor(Color.BLACK)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val motionEventX = motionEvent.x
        val motionEventY = motionEvent.y

        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("SpotLight", "action down: $gameOver")
                shouldDrawSpotLight = true
                if (gameOver) {
                    // New Game
                    gameOver = false
                    setupWinnerRect()
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.d("SpotLight", "action up: $gameOver")
                shouldDrawSpotLight = false
                gameOver = winnerRect.contains(motionEventX, motionEventY)
                Log.d("SpotLight", "Did hit? -> $gameOver")
            }
        }
        shaderMatrix.setTranslate(
            motionEventX - spotlight.width / 2.0f,
            motionEventY - spotlight.height / 2.0f
        )
        shader.setLocalMatrix(shaderMatrix)
        invalidate()
        return true
    }


}