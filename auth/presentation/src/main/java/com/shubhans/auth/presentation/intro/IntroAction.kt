package com.shubhans.auth.presentation.intro

sealed interface IntroAction{
    data object onSignUpClick:IntroAction
    data object onSignInClick:IntroAction
}