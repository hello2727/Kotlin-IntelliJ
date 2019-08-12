package com.example.bmicalculator

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //이전에 입력한 값을 읽어오기
        loadData()

        resultButton.setOnClickListener {
            //마지막에 입력한 내용 저장
            saveData(heightEditText.text.toString().toInt(),
                weightEditText.text.toString().toInt())

            startActivity<ResultActivity>(
                "weight" to weightEditText.text.toString(),
                "height" to heightEditText.text.toString()
            )
        }
    }
    //데이터 저장하기
    private fun saveData(height: Int, weight: Int){
        var pref = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = pref.edit()

        editor.putInt("KEY_HEIGHT", height)
            .putInt("KEY_WEIGHT", weight)
            .apply()
    }
    //데이터 불러오기
    private fun loadData(){
        var pref = PreferenceManager.getDefaultSharedPreferences(this)
        var height = pref.getInt("KEY_HEIGHT", 0)
        var weight = pref.getInt("KEY_WEIGHT", 0)

        if(height != 0 && weight != 0){ //저장된 값이 없을 때는 아무것도 안함
            heightEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}
