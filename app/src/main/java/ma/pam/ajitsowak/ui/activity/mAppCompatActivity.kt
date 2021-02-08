package ma.pam.ajitsowak.ui.activity

import android.app.Activity
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import ma.pam.ajitsowak.MyApp
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.room.CartItem
import ma.pam.ajitsowak.utils.Constants
import ma.pam.ajitsowak.utils.getSharedPrefInstance
import ma.pam.ajitsowak.woolib.models.Product

open class mAppCompatActivity : AppCompatActivity() {
    var progressDialog:Dialog? = null

    fun showProgress(show: Boolean ) {

        if (progressDialog == null) {

            progressDialog  = Dialog(this).apply {
                window!!.setBackgroundDrawable(ColorDrawable(0))
                setContentView(R.layout.custom_dialog)
            }
        }
            // if(getPrimaryColor().isNotEmpty()){ progressDialog!!.tv_progress_msg.changePrimaryColor() }
            when {
                show -> {
                    if (!isFinishing) {
                        progressDialog?.setCanceledOnTouchOutside(false)
                        progressDialog?.show()
                    }
                }
                else -> try {
                    if (progressDialog!!.isShowing && !isFinishing) {
                        progressDialog?.dismiss()
                    }
                } catch (e: Exception) {
                }
            }
        }


}