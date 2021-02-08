package ma.pam.ajitsowak.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.format.DateFormat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ma.pam.ajitsowak.MyApp
import ma.pam.ajitsowak.MyApp.getAppInstance
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.Constants.SharedPref.ACCENTCOLOR
import ma.pam.ajitsowak.utils.Constants.SharedPref.BACKGROUNDCOLOR
import ma.pam.ajitsowak.utils.Constants.SharedPref.BILLING
import ma.pam.ajitsowak.utils.Constants.SharedPref.DEFAULT_CURRENCY
import ma.pam.ajitsowak.utils.Constants.SharedPref.DEFAULT_CURRENCY_FORMATE
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_CART_COUNT
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_ORDER_COUNT
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_PRODUCT_DETAIL
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_USER_ADDRESS
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_WISHLIST_COUNT
import ma.pam.ajitsowak.utils.Constants.SharedPref.LANGUAGE
import ma.pam.ajitsowak.utils.Constants.SharedPref.PRIMARYCOLOR
import ma.pam.ajitsowak.utils.Constants.SharedPref.TEXTPRIMARYCOLOR
import ma.pam.ajitsowak.utils.Constants.SharedPref.TEXTSECONDARYCOLOR
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_DISPLAY_NAME
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_EMAIL
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_FIRST_NAME
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_ID
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_LAST_NAME
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_NICE_NAME
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_PROFILE
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_ROLE
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_TOKEN
import ma.pam.ajitsowak.utils.Constants.SharedPref.USER_USERNAME
import ma.pam.ajitsowak.woolib.models.BillingAddress
import ma.pam.ajitsowak.woolib.models.ShippingAddress
import java.text.SimpleDateFormat
import java.util.*


fun getUserId(): String = getSharedPrefInstance().getStringValue(USER_ID)
fun getDefaultCurrency(): String = getSharedPrefInstance().getStringValue(DEFAULT_CURRENCY)
fun getLanguage(): String = getSharedPrefInstance().getStringValue(LANGUAGE)
fun getDisplayName(): String = getSharedPrefInstance().getStringValue(USER_DISPLAY_NAME)
fun getLastName(): String = getSharedPrefInstance().getStringValue(USER_LAST_NAME)
fun getEmail(): String = getSharedPrefInstance().getStringValue(USER_EMAIL)
fun getCartCount(): Int = getSharedPrefInstance().getIntValue(KEY_CART_COUNT, 0)
fun getWishListCount(): String = getSharedPrefInstance().getIntValue(KEY_WISHLIST_COUNT, 0).toString()
fun getOrderCount(): String = getSharedPrefInstance().getIntValue(KEY_ORDER_COUNT, 0).toString()
fun getApiToken(): String = getSharedPrefInstance().getStringValue(USER_TOKEN)
fun getUserProfile(): String = getSharedPrefInstance().getStringValue(USER_PROFILE)
fun getPrimaryColor(): String { return getSharedPrefInstance().getStringValue(PRIMARYCOLOR) }
fun getAccentColor(): String { return getSharedPrefInstance().getStringValue(ACCENTCOLOR) }
fun getButtonColor(): String { return getSharedPrefInstance().getStringValue(PRIMARYCOLOR) }
fun getTextPrimaryColor(): String { return getSharedPrefInstance().getStringValue(TEXTPRIMARYCOLOR) }
fun getTextSecondaryColor(): String { return getSharedPrefInstance().getStringValue(TEXTSECONDARYCOLOR) }
fun getBackgroundColor(): String { return getSharedPrefInstance().getStringValue(BACKGROUNDCOLOR) }


fun convertOrderDataToLocalDate(ourDate: Date?): String? {
    if (ourDate == null) return "no date"
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    return dateFormatter.format(ourDate)
}

fun convertToLocalDate(ourDate: Date?): String? {
    if (ourDate == null) return "no date"
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    return dateFormatter.format(ourDate)
}

/**
 * Add shared preference related to user session here
 */
fun clearLoginPref() {
    getSharedPrefInstance().removeKey(USER_ID)
    getSharedPrefInstance().removeKey(USER_DISPLAY_NAME)
    getSharedPrefInstance().removeKey(USER_EMAIL)
    getSharedPrefInstance().removeKey(USER_NICE_NAME)
    getSharedPrefInstance().removeKey(USER_TOKEN)
    getSharedPrefInstance().removeKey(USER_FIRST_NAME)
    getSharedPrefInstance().removeKey(USER_LAST_NAME)
    getSharedPrefInstance().removeKey(USER_PROFILE)
    getSharedPrefInstance().removeKey(USER_ROLE)
    getSharedPrefInstance().removeKey(USER_USERNAME)
    getSharedPrefInstance().removeKey(KEY_USER_ADDRESS)
    getSharedPrefInstance().removeKey(KEY_CART_COUNT)
    getSharedPrefInstance().removeKey(KEY_ORDER_COUNT)
    getSharedPrefInstance().removeKey(KEY_WISHLIST_COUNT)
}

fun getSharedPrefInstance(): SharedPrefUtils {
    return if (MyApp.getSharedPrefUtils() == null) {
        MyApp.setSharedPrefUtils(SharedPrefUtils())
        MyApp.getSharedPrefUtils()!!
    } else {
        MyApp.getSharedPrefUtils()!!
    }
}


fun ImageView.displayBlankImage(aContext: Context, aPlaceHolderImage: Int) {
    Glide.with(aContext)
        .load(aPlaceHolderImage)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun Context.fontSemiBold(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_SemiBold))
}

fun Context.fontBold(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_Bold))
}

fun Context.fontRegular(): Typeface? {
    return Typeface.createFromAsset(assets, getString(R.string.font_regular))
}

fun Activity.getAlertDialog(aMsgText: String, aTitleText: String = getString(R.string.lbl_dialog_title), aPositiveText: String = getString(R.string.lbl_yes), aNegativeText: String = getString(R.string.lbl_no), onPositiveClick: (dialog: DialogInterface, Int) -> Unit, onNegativeClick: (dialog: DialogInterface, Int) -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(aTitleText)
    builder.setMessage(aMsgText)
    builder.setPositiveButton(aPositiveText) { dialog, which ->
        onPositiveClick(dialog, which)
    }
    builder.setNegativeButton(aNegativeText) { dialog, which ->
        onNegativeClick(dialog, which)
    }
    return builder.create()
}


fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun isNetworkAvailable(): Boolean {
    val info = getAppInstance().getConnectivityManager().activeNetworkInfo
    return info != null && info.isConnected
}


fun Activity.productLayoutParams(): RelativeLayout.LayoutParams {
    val width = (getDisplayWidth() / 4.2).toInt()
    val imgHeight = width + (width / 12)
    return RelativeLayout.LayoutParams(width, imgHeight)
}

fun Activity.productLayoutParamsForDealOffer(): RelativeLayout.LayoutParams {
    val width = (getDisplayWidth() / 3.2).toInt()
    val imgHeight = width + (width / 10)
    return RelativeLayout.LayoutParams(width, imgHeight)
}

fun Context.getDisplayWidth(): Int = resources.displayMetrics.widthPixels

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun TextView.applyStrike() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun String.currencyFormat(code: String = "MAD"): String {
    return if (this.isEmpty()) "" else {
        "$this MAD"
    }
    /*return when (code) {
        "USD" -> "$$this"
        "INR" -> "₹$this"
        else -> "₹$this"
    }*/
}
fun RecyclerView.rvItemAnimation() {
    layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
}

fun String.getHtmlString(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

fun getbillingList(): BillingAddress {
    val string = getSharedPrefInstance().getStringValue(BILLING, "")
    if (string.isEmpty()) {
        return BillingAddress()
    }
    return Gson().fromJson(string, object : TypeToken<BillingAddress>() {}.type)
}

fun getShippingList(): ShippingAddress {
    val string = getSharedPrefInstance().getStringValue(Constants.SharedPref.SHIPPING, "")
    if (string.isEmpty()) {
        return ShippingAddress()
    }
    return Gson().fromJson(string, object : TypeToken<ShippingAddress>() {}.type)
}

