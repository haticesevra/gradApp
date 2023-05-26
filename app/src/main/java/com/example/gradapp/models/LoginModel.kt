package com.example.gradapp.models

class LoginModel {
    var status: String = ""
    var user: User = User()
    var token: String = ""

    class User {
        var _id: String = ""
        var username: String = ""
        var email: String = ""
        var password: String = ""
        var date: String = ""
        var __v: Int = 0
    }
}
