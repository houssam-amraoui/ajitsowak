package ma.pam.ajitsowak.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import ma.pam.ajitsowak.MyApp.getAppInstance
import ma.pam.ajitsowak.R



fun ImageView.loadImageFromUrl(aImageUrl: String, aPlaceHolderImage: Int = R.drawable.placeholder, aErrorImage: Int = R.drawable.placeholder) {
    if (!aImageUrl.isEmpty()) {
        Glide.with(getAppInstance())
            .load(aImageUrl)
            .placeholder(aPlaceHolderImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(aErrorImage)
            .into(this)
    } else {
        displayBlankImage(getAppInstance(), aPlaceHolderImage)
    }
}
fun ImageView.loadImageFromDrawable(@DrawableRes aPlaceHolderImage: Int) {
    Glide.with(getAppInstance()).load(aPlaceHolderImage).diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
}

