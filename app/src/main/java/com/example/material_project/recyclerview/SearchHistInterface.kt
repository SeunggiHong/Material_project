package com.example.material_project.recyclerview

interface SearchHistInterface {

    //검색 아이템 삭제 버튼 클릭
    fun onSearchItemDeleteClicked(position: Int)

    // 검색 버튼 클릭
    fun onSearchItemClicked(position: Int)

}