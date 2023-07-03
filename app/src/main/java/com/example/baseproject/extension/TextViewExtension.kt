package com.example.baseproject.extension

import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

fun AppCompatTextView.makeLink(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for(link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                widget.invalidate()
                link.second.onClick(widget)
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ds.linkColor
                super.updateDrawState(ds)
            }
        }
        val start = this.text.findAnyOf(listOf(link.first))?.first ?: 0
        val end = start + link.first.length
        spannableString.setSpan(clickableSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}