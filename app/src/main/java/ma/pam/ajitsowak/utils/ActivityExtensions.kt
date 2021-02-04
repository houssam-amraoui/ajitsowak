package ma.pam.ajitsowak.utils

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.fragment.WishListFragment

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
        beginTransaction().func().commit()

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) =
        supportFragmentManager.inTransaction { replace(frameId, fragment) }

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) =
        supportFragmentManager.inTransaction { add(frameId, fragment) }

fun AppCompatActivity.removeFragment(fragment: Fragment) =
        supportFragmentManager.inTransaction { remove(fragment) }

fun AppCompatActivity.showFragment(fragment: Fragment) = supportFragmentManager.inTransaction {
    setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
    show(fragment)
}

fun AppCompatActivity.hideFragment(fragment: Fragment) = supportFragmentManager.inTransaction {
    hide(fragment)
}


fun AppCompatActivity.setToolbar(mToolbar: Toolbar) {
    setSupportActionBar(mToolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)
    // mToolbar.changeToolbarFont()
    //  mToolbar.navigationIcon!!.setColorFilter(Color.parseColor(getTextTitleColor()), PorterDuff.Mode.SRC_ATOP)
    // mToolbar.setTitleTextColor(Color.parseColor(getTextTitleColor()))
}

fun AppCompatActivity.setDetailToolbar(mToolbar: Toolbar) {
    setSupportActionBar(mToolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    mToolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace)

    mToolbar.setNavigationOnClickListener {
        onBackPressed()
    }


    //  mToolbar.changeToolbarFont()
    // mToolbar.navigationIcon!!.setColorFilter(Color.parseColor(getAccentColor()), PorterDuff.Mode.SRC_ATOP)
    // mToolbar.setTitleTextColor(Color.parseColor(getTextTitleColor()))
}

var progressDialog: Dialog? = null

fun AppCompatActivity.showProgress(show: Boolean) {

    if (progressDialog == null) {
        Log.d("nihahah", "enit")
        progressDialog = Dialog(this)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        progressDialog!!.setContentView(R.layout.custom_dialog)
        if(getPrimaryColor().isNotEmpty()){
           // progressDialog!!.tv_progress_msg.changePrimaryColor()
        }
    }
    when {
        show -> {
            if (!isFinishing) {
                progressDialog!!.setCanceledOnTouchOutside(false)
                progressDialog!!.show()
            }
        }
        else -> try {
            if (progressDialog!!.isShowing && !isFinishing) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
