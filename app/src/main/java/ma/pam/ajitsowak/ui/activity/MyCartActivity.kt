package ma.pam.ajitsowak.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.fragment.MyCartFragment
import ma.pam.ajitsowak.utils.addFragment
import ma.pam.ajitsowak.utils.setDetailToolbar

class MyCartActivity : AppCompatActivity() {

    private var myCartFragment: MyCartFragment = MyCartFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setDetailToolbar(toolbar)
        title = getString(R.string.menu_my_cart)
        //mAppBarColor()
        //rlMain.changeBackgroundColor()
        //myCartFragment.invalidateCartLayout()

        addFragment(myCartFragment, R.id.container)

    }


}
