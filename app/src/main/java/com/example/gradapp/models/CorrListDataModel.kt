package com.example.gradapp.models

data class  CorrListDataModel(
    val status: String,
    val data: List<CorrListItem>
)

data class CorrListItem(
    val _id: String,
    val name: String,
    val corrParameter: String,
    val filteredData: String,
    val __v: Int
)
