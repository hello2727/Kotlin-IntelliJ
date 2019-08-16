package com.example.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View

class TiltView(context: Context?) : View(context) {
    private val greenPaint: Paint = Paint()
    private val blackPaint: Paint = Paint()

    private var cX: Float = 0f
    private var cY: Float = 0f

    private var xCoord: Float = 0f
    private var yCoord: Float = 0f

    //센서값받기
    fun onSensorEvent(event: SensorEvent) {
        //화면을 가로로 돌렸으므로 x축과 y축을 서로 바꿈
        yCoord = event.values[0]*20
        xCoord = event.values[1]*20

        invalidate()
    }
    //녹색 페인트와 검은색 테두리 페인트를 준비
    init {
        //녹색페인트
        greenPaint.color = Color.GREEN
        //검은색 테두리 페인트
        blackPaint.style = Paint.Style.STROKE
    }
    //뷰의 크기가 결정되면 호출한 onSizeChanged()메서드에서 중점좌표(cX, cY)를 계산
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cX = w / 2f
        cY = h / 2f
    }
    //위에서 정한 x좌표와 y좌표에 각각 cx, cy를 더함
    override fun onDraw(canvas: Canvas?) {
        //바깥 원
        canvas?.drawCircle(cX, cY, 100f, blackPaint)
        //녹색 원
        canvas?.drawCircle(xCoord + cX, yCoord + cY, 100f, greenPaint)
        //가운데 십자가
        canvas?.drawLine(cX - 20, cY, cX + 20, cY, blackPaint)
        canvas?.drawLine(cX, cY - 20, cX, cY + 20, blackPaint)
    }
}