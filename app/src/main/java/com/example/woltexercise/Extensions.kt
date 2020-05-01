package com.example.woltexercise

import android.view.View

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}