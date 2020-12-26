package com.gigaworks.tech.bloodbank.util

import android.text.Editable
import android.text.TextWatcher

class TextErrorWatcher(
    private val textChanged: (Editable) -> Unit = {}
) :
    TextWatcher {
    override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        //
    }

    override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
        //
    }

    override fun afterTextChanged(editable: Editable) {
        textChanged(editable)
    }
}