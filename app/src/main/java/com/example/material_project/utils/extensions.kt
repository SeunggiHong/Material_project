package com.example.material_project.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onMyTextChanged(completion: (Editable?) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            completion(editable)
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    })
}

fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith(("}"))
}

fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith(("]"))
}