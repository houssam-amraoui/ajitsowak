package ma.pam.ajitsowak.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.github.loadingview.LoadingDialog
import ma.pam.ajitsowak.MyApp

import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.fragment.HomeFragment1
import ma.pam.ajitsowak.utils.*


class DashBoardActivity : AppCompatActivity() {

    private lateinit var mHomeFragment: Fragment
  //  private val mWishListFragment = WishListFragment()
  //  private val mViewAllProductFragment = ViewAllProductFragment()
    private var selectedFragment: Fragment? = null
    private var mModeFlag: Boolean = false
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var llLeftDrawer:LinearLayout
    private lateinit var main:View
    private lateinit var toolbar:Toolbar
    private lateinit var tvHome:TextView
    private lateinit var tvOrder:TextView
    private lateinit var tvWishlist:TextView
    private lateinit var tvCategories:TextView
    private lateinit var tvCart:TextView
    lateinit var tvUser:TextView
    private lateinit var tvAbout:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        llLeftDrawer = findViewById(R.id.llLeftDrawer)
        toolbar = findViewById(R.id.toolbar)
        tvHome = findViewById(R.id.tvHome)
        tvOrder = findViewById(R.id.tvOrder)
        tvWishlist = findViewById(R.id.tvWishlist)
        tvCategories = findViewById(R.id.tvCategories)
        tvCart = findViewById(R.id.tvCart)
        tvUser = findViewById(R.id.tvUser)
        tvAbout = findViewById(R.id.tvAbout)

        main = findViewById(R.id.main)

        mHomeFragment = HomeFragment1()
        setToolbar(toolbar)
        setUpDrawerToggle()

        loadHomeFragment()

        tvHome.setOnClickListener {
            loadFragment(mHomeFragment)
            title = "home"
            closeDrawer()
        }
        tvOrder.setOnClickListener {
            startActivity(Intent(this@DashBoardActivity,OrderActivity::class.java))
            closeDrawer()

        }

        tvWishlist.setOnClickListener {
            closeDrawer()
            startActivity(Intent(this@DashBoardActivity,WishlistActivity::class.java))
        }

        tvCategories.setOnClickListener {
            closeDrawer()
            startActivity(Intent(this@DashBoardActivity,CategoryActivity::class.java))

        }
        tvCart.setOnClickListener {
            closeDrawer()
            startActivity(Intent(this@DashBoardActivity,MyCartActivity::class.java))
        }
        tvUser.setOnClickListener {
            closeDrawer()
            startActivity(Intent(this@DashBoardActivity,EditProfileActivity::class.java))
        }
        tvAbout.setOnClickListener {

            showProgress(true)
            Handler(Looper.getMainLooper()).postDelayed({
                showProgress(false)
            },3000)





           // startActivity(Intent(this@DashBoardActivity,AboutActivity::class.java))
            closeDrawer()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

        }
    }


    private fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(llLeftDrawer))
            drawerLayout.closeDrawer(llLeftDrawer)
    }

    private fun setUpDrawerToggle() {
        val toggle = object :
            ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            private val scaleFactor = 4f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset

                when (MyApp.language) {
                    "ar" -> main.translationX = -slideX
                    else -> main.translationX = slideX
                }
                main.scaleX = 1 - slideOffset / scaleFactor
                main.scaleY = 1 - slideOffset / scaleFactor
            }
        }

        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.drawerElevation = 0f
        toggle.isDrawerIndicatorEnabled = false
        toolbar.setNavigationIcon(R.drawable.ic_drawer)

       // toolbar.navigationIcon!!.setColorFilter(Color.parseColor(getTextTitleColor()), PorterDuff.Mode.SRC_ATOP)

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }



    private fun loadFragment(aFragment: Fragment) {
        if (selectedFragment != null) {
            if (selectedFragment == aFragment) return
            hideFragment(selectedFragment!!)
        }
        if (aFragment.isAdded) {
            showFragment(aFragment)
        } else {

            addFragment(aFragment, R.id.container)
        }
        selectedFragment = aFragment
    }

    private fun loadHomeFragment() {
        loadFragment(mHomeFragment)
        title = getString(R.string.home)
    }



    override fun onBackPressed() {
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            !mHomeFragment.isVisible -> loadHomeFragment()
         //   mViewAllProductFragment.isVisible -> { loadHomeFragment() }

            else -> super.onBackPressed()
        }
    }


/*
    private fun changeColor() {
        txtDisplayName.changeTextPrimaryColor()
        txtDisplayEmail.changeTextPrimaryColor()
        tvHome.changeTextPrimaryColor()
        tvCart.changeTextPrimaryColor()
        tvWishlist.changeTextPrimaryColor()
        tvCategories.changeTextPrimaryColor()
        tvOrder.changeTextPrimaryColor()
        tvChangePwd.changeTextPrimaryColor()
        tvMode.changeTextPrimaryColor()
        tvSignIn.changeTextPrimaryColor()
        tvAbout.changeTextPrimaryColor()
        setTextViewDrawableColor(tvHome, getTextPrimaryColor())
        setTextViewDrawableColor(tvCart, getTextPrimaryColor())
        setTextViewDrawableColor(tvWishlist, getTextPrimaryColor())
        setTextViewDrawableColor(tvCategories, getTextPrimaryColor())
        setTextViewDrawableColor(tvOrder, getTextPrimaryColor())
        setTextViewDrawableColor(tvChangePwd, getTextPrimaryColor())
        setTextViewDrawableColor(tvMode, getTextPrimaryColor())
        setTextViewDrawableColor(tvSignIn, getTextPrimaryColor())
        setTextViewDrawableColor(tvAbout, getTextPrimaryColor())
    }
*/

}
