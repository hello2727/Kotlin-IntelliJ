package com.example.stopwatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var lap = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //시작과 일시정지 이벤트 구현
        fab.setOnClickListener {
            isRunning = !isRunning //변수 값 반전

            if(isRunning){
                start()
            }else{
                pause()
            }
        }
        //랩 타임 버튼에 이벤트 연결
        lapButton.setOnClickListener {
            recordLapTime()
        }
        //초기화 버튼에 이벤트 연결
        resetFab.setOnClickListener {
            reset()
        }
    }

    //타이머 시작 구현
    private fun start() {
        fab.setImageResource(R.drawable.ic_pause_black_24dp)

        timerTask = timer(period = 10){//0.01초마다
            time++ //이 변수를 증가
            var sec = time / 100
            var milli = time % 100
            runOnUiThread { //워커 스레드(오래 걸리는 작업을 보이지 않는 곳에서 처리)
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }
    //타이머 일시정지
    private fun pause() {
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        timerTask?.cancel()
    }

    //랩 타임 기록
    private fun recordLapTime() {
        var lapTime = this.time
        var textView = TextView(this)
        textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"

        // 맨 위에 랩타임 추가
        lapLayout.addView(textView, 0)
        lap++
    }

    //타이머 초기화
    private fun reset() {
        timerTask?.cancel() //실행 중인 타이머가 있다면 취소

        //모든 변수 초기화
        time = 0
        isRunning = false
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        secTextView.text = "0"
        milliTextView.text ="00"
    }
}
