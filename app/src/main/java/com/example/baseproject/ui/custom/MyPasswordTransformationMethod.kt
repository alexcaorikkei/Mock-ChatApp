package com.example.baseproject.ui.custom

import android.text.method.PasswordTransformationMethod
import android.view.View

class MyPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return PasswordCharSequence(source)
    }
}

private class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

    override val length: Int get() = mSource.length

    override fun get(index: Int): Char = '*'

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        mSource.subSequence(startIndex, endIndex)
}