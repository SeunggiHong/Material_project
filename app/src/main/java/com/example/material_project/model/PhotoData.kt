package com.example.material_project.model

import java.io.Serializable

data class PhotoData(var thumbnail: String?,
                     var author: String?,
                     var createdAt: String?,
                     var likesCount: Int?) :Serializable {
}