package com.example.ka.googlemapapi.model

//  private String email;
//    private String user_id;
//    private String username;
//    private String avatar;

class User private constructor(val email: String?,
                               val userId: String?,
                               val userName : String?,
                               val avatar: String?){

    data class Builder(
        var email: String? = null,
        var userId: String?= null,
        var userName : String? = null,
        var avatar: String? = null){

        fun email(email:String) = apply { this.email = email }
        fun userId(userId: String) = apply { this.userId = userId }
        fun userName(userName: String) = apply { this.userName = userName }
        fun avatar(avatar: String) = apply { this.avatar = avatar }
        fun build() = User(email, userId, userName, avatar)
    }
}