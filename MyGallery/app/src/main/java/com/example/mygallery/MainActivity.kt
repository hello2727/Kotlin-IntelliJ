package com.example.mygallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //권한이 부여되었는지 확인
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.
                PERMISSION_GRANTED) {
            //권한이 허용되지 않음
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.
                    permission.READ_EXTERNAL_STORAGE
                )) {
                //이전에 이미 권한이 거부되었을 때 설명
                alert("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다",
                    "권한이 필요한 이유") {
                    yesButton {
                        //권한 요청
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE)
                    }
                    noButton {}
                }.show()
            } else {
                //권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        }else{
            //권한이 이미 허용됨
            getAllPhotos()
        }
    }
    //권한 요청에 대한 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty()
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //권한 허용됨
                    getAllPhotos()
                }else{
                    //권한 거부
                    toast("권한 거부 됨")
                }
                return
            }
        }
    }
    private fun getAllPhotos() {
        //모든 사진 정보 가져오기
        var cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, //가져올 항목 배열
            null, //조건
            null, //조건
            MediaStore.Images.ImageColumns.DATE_TAKEN+" DESC") //찍은 날짜 내림차순

        //프래그먼트를 아이템으로 하는 ArrayList를 생성
        val fragments = ArrayList<Fragment>()
        //사진 정보 로그로 표시
        if(cursor != null) {
            while(cursor.moveToNext()) {
                //사진 경로 uri 가져오기
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.
                    Media.DATA))
                Log.d("MainActivity", uri)
                fragments.add(PhotoFragment.newInstance(uri)) //프래그먼트를 생성하면서 fragments를 리스트에 추가
            }
            cursor.close()
        }
        //어댑터
        val adapter = MyPaperAdapter(supportFragmentManager)
        adapter.updateFragments(fragments)
        viewPaper.adapter = adapter
        //3초마다 자동 슬라이드
        timer(period = 3000) {
            runOnUiThread {
                if(viewPaper.currentItem < adapter.count - 1) { //현재 페이지가 마지막 페이지가 아니라면
                    viewPaper.currentItem = viewPaper.currentItem+1 //다음 페이지로 변경하고
                }else{ //마지막 페이지라면
                    viewPaper.currentItem = 0 //첫페이지로 변경
                }
            }
        }
    }
}
