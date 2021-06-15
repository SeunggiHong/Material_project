package com.example.material_project.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.material_project.utils.Constants.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat

import java.util.*

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

fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("HH:mm:ss")
    return format.format(this)
}

@ExperimentalCoroutinesApi
fun EditText.textChangesToFlow(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Unit
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d(TAG, "onTextChanged: called : $text")
                // 값보내기
                offer(text)
            }

            override fun afterTextChanged(p0: Editable?) {
                Unit
            }

        }
        addTextChangedListener(listener)
        // 콜백이 사라질때 실행
        awaitClose {
            Log.d(TAG, "textChangesToFlow: awaitClose")
            removeTextChangedListener(listener)
        }
    }.onStart {
        Log.d(TAG, "textChangesToFlow: onStart")
        // emit 으로 이벤트를 전달
        emit(text)
    }
}