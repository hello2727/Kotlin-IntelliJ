package com.example.todolist

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//할 일 데이터 클래스
class Todo (
    @PrimaryKey var id: Long = 0,
    var title: String = "",
    var date: Long = 0
) : RealmObject() {

}