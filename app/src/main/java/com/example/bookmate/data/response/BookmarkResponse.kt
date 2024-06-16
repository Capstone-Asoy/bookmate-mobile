package com.example.bookmate.data.response

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(

    @field:SerializedName("bookmarks")
    val bookmarks: List<BookmarksItem>
)

data class BookmarksItem(

    @field:SerializedName("bookmark_id")
    val bookmarkId: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("penulis")
    val penulis: String,

    @field:SerializedName("judul")
    val judul: String,

    @field:SerializedName("books_id")
    val id: Int
)
