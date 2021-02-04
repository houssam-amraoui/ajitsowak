package ma.pam.ajitsowak.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.fragment.WishListFragment
import ma.pam.ajitsowak.utils.addFragment
import ma.pam.ajitsowak.utils.setDetailToolbar
import ma.pam.ajitsowak.utils.setToolbar


class WishlistActivity : AppCompatActivity() {

    private var myCartFragment: WishListFragment = WishListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)


        setDetailToolbar(findViewById(R.id.toolbar))
        title = getString(R.string.lbl_wish_list)
        //mAppBarColor()
        addFragment(myCartFragment, R.id.container)

    }

}
