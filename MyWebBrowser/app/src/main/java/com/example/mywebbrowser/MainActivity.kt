package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //웹뷰 기본 설정
        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
        //컨텍스트 메뉴 등록
        registerForContextMenu(webView)

        webView.loadUrl("http://www.google.com")

        //소프트 키보드의 검색 버튼 동작 정의
        urlEditText.setOnEditorActionListener { _, actionId, _ -> // 에디트텍스트가 선택되고 글자가 입력될 때마다 호출
            if(actionId == EditorInfo.IME_ACTION_SEARCH) { // 검색 버튼이 눌렸는지 확인
                webView.loadUrl(urlEditText.text.toString()) // 검색창에 입력한 주소를 웹뷰에 전달하여 로딩
                true
            }else{
                false
            }
        }
    }
    //뒤로가기 동작 재정의
    override fun onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack()
        }else{
            super.onBackPressed() //원래 동작을 수행
        }
    }
    //옵션메뉴 리소스 지정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu) //메뉴 리소스를 액티비티의 메뉴로 표시
        return true // true를 반환하면 액티비티에 메뉴가 있다고 인식
    }
    //옵션 메뉴의 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) { //메뉴 아이템으로 분기를 수행
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("http://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("http://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("http://www.daum.net")
                return true
            }
            R.id.action_call -> {
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:031-123-4567")
                if(intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                sendSMS("031-123-4567", webView.url)
                return true
            }
            R.id.action_email -> {
                email("test@example.com", "좋은 사이트", webView.url)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //컨텍스트 메뉴 작성
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }
    //컨텍스트 메뉴 클릭 이벤트 처리
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                share(webView.url)
                return true
            }
            R.id.action_browser -> {
                browse(webView.url)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
