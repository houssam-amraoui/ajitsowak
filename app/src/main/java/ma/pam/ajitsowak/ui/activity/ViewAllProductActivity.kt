package ma.pam.ajitsowak.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.fragment.ViewAllProductFragment
import ma.pam.ajitsowak.utils.Constants
import ma.pam.ajitsowak.utils.addFragment
import ma.pam.ajitsowak.utils.setToolbar


class ViewAllProductActivity : AppCompatActivity() {

    private var mFragment: ViewAllProductFragment? = null
    private lateinit var toolbar:Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all)
        toolbar = findViewById(R.id.toolbar)
        setToolbar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        title = intent.getStringExtra(Constants.KeyIntent.TITLE)
        val mViewAllId = intent.getIntExtra(Constants.KeyIntent.VIEWALLID, 0) // type
        val mCategoryId = intent.getIntExtra(Constants.KeyIntent.KEYID, -1) // id category

        mFragment =  ViewAllProductFragment.getNewInstance(mViewAllId, mCategoryId)

        addFragment(mFragment!!, R.id.fragmentContainer)
    }
}