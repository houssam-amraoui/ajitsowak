package ma.pam.ajitsowak.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.Constants.SharedPref.SHOW_SWIPE
import ma.pam.ajitsowak.utils.getSharedPrefInstance

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        if (getSharedPrefInstance().getBooleanValue(SHOW_SWIPE)) {

            val intent = Intent(this, DashBoardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()
        }

        makeTransaprant()
        val adapter = WalkAdapter()
        findViewById<ViewPager>(R.id.theme3Viewpager).adapter = adapter
       // dots.attachViewPager(theme3Viewpager)
       // dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
        findViewById<TextView>(R.id.btnStatShopping).setOnClickListener {
            val intent = Intent(this, DashBoardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            getSharedPrefInstance().setValue(SHOW_SWIPE, true)
            finish()
        }
    //    changeColor()
    }

    fun AppCompatActivity.makeTransaprant() {
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    internal inner class WalkAdapter : PagerAdapter() {

        private var mHeading =
            arrayOf(
                getString(R.string.lbl_signin_up),
                getString(R.string.lbl_product_quality),
                getString(R.string.lbl_make_delicious_dishes)
            )

        private var mSubHeading = arrayOf(
            getString(R.string.lbl_dummy_text1),
            getString(R.string.lbl_dummy_text2),
            getString(R.string.lbl_dummy_text3)
        )

        private val mImg = intArrayOf(
            R.drawable.ic_walk_1,
            R.drawable.ic_trends,
            R.drawable.ic_walk_3
        )

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(R.layout.item_intro, container, false)
            view.findViewById<ImageView>(R.id.imgWalk).setImageResource(mImg[position])
            view.findViewById<TextView>(R.id.tvHeading).text = mHeading[position]
           // view.findViewById<TextView>(R.id.tvHeading).changeAccentColor()
            view.findViewById<TextView>(R.id.tvSubHeading).text = mSubHeading[position]
          //  view.findViewById<TextView>(R.id.tvSubHeading).changeTextSecondaryColor()
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return mImg.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }
    }

  /*  private fun changeColor() {
        btnStatShopping.changeBackgroundTint(getPrimaryColor())
    }*/

}
