package com.example.material_project.utils

object Constants {
    const val TAG: String = "logMsg"
}

enum class SEARCH_TYPE {
    PHOTO,
    USER
}

enum class RESPONSE_STATUS{
    SUCCESS,
    FAIL,
    NO_CONTENT
}

object UNSPLASH_API {
    const val BASE_URL:String = "https://api.unsplash.com/"
    const val CLIENT_ID:String = "CKYk9pc2GVMAURhBCjdAQjXPoM3ZmM_5s0hxFHN8hEo"
    const val SEARCH_PHOTO:String = "search/photos"
    const val SEARCH_USERS:String = "search/users"
}