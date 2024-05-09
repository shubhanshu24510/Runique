package com.shubhans.auth.data.di

import android.util.Patterns
import com.shubhans.auth.domain.PattenValidator

object EmailPatternValidator: PattenValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

}