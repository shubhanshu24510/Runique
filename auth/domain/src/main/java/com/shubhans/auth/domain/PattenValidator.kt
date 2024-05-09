package com.shubhans.auth.domain

interface PattenValidator {
    fun matches(value:String):Boolean
}