package ma.pam.ajitsowak.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.textToString(): String = this.text.toString()

fun EditText.checkIsEmpty(): Boolean = text == null || "" == textToString() || text.toString().equals("null", ignoreCase = true)

fun EditText.isValidEmail(): Boolean = !TextUtils.isEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches()

fun EditText.isValidPhoneNumber(): Boolean = text.matches("^(((\\+?\\(91\\))|0|((00|\\+)?91))-?)?[7-9]\\d{9}$".toRegex())

fun EditText.showError(error: String) {
    this.error = error
    this.showSoftKeyboard()
}
fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun EditText.validPassword(): Boolean = text.length < 6

fun EditText.isValidText(): Boolean = text.matches("[a-zA-Z]+".toRegex())
