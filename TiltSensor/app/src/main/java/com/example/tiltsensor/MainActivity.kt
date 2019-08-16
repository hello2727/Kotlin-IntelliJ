package com.example.tiltsensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { //센서 정밀도가 변경되면 호출
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    //센서값 읽기
    override fun onSensorChanged(event: SensorEvent?) { //센서값이 변경되면 호출
        //센서값이 변경되면 호출됨
        //values[0] : x축 값 : 위로 기울이면 -10~0, 아래로 기울이면 0~10
        //values[1] : y축 값 : 왼쪽으로 기울이면 -10~0, 오른쪽으로 기울이면 0~10
        //values[2] : z축 값 : 미사용
        event?.let {
            Log.d("MainActivity", "onSensorChanged: x :" +
            " ${event.values[0]}, y : ${event.values[1]}, z : ${event.values[2]}")

            tiltView.onSensorEvent(event) //센서값 전달
        }
    }
    //sensorManager 준비
    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    //센서 등록
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, //사용할 센서를 등록
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), //사용할 센서 종류를 지정
            SensorManager.SENSOR_DELAY_NORMAL)
    }
    //센서 해제
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    //TiltView를 화면에 배치
    private lateinit var tiltView: TiltView //TiltView의 늦은 초기화 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        //화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //화면이 가로 모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)

        tiltView = TiltView(this)
        setContentView(tiltView)

        setContentView(R.layout.activity_main)
    }

}
