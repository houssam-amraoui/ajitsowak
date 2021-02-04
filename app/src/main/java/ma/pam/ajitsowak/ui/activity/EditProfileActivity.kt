package ma.pam.ajitsowak.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.gson.Gson
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.woolib.models.BillingAddress
import ma.pam.ajitsowak.woolib.models.Customer
import ma.pam.ajitsowak.woolib.models.ShippingAddress


class EditProfileActivity : AppCompatActivity() {


    lateinit var ivProfile:ImageView
    lateinit var tvCountWishList:TextView
    lateinit var tvOrderCount:TextView
    lateinit var cbCheck:CheckBox

    lateinit var edtShippingFName:EditText
    lateinit var edtShippingLName:EditText
    lateinit var edtShippingCompany:EditText
    lateinit var edtShippingAdd1:EditText
    lateinit var edtShippingAdd2:EditText
    lateinit var edtShippingCity:EditText
    lateinit var edtShippingPinCode:EditText


    lateinit var edtBillingFName:EditText
    lateinit var edtBillingLName:EditText
    lateinit var edtBillingCompany:EditText
    lateinit var edtBillingAdd1:EditText
    lateinit var edtBillingAdd2:EditText
    lateinit var edtBillingCity:EditText
    lateinit var edtBillingPinCode:EditText


    lateinit var edtBillingPhone:EditText
    lateinit var edtBillingEmail:EditText

    lateinit var btnSaveProFile:Button
    lateinit var editProfileImage:CoordinatorLayout
    lateinit var llOrder:LinearLayout
    lateinit var llWishList:LinearLayout

    lateinit var edtFirstName:EditText
    lateinit var edtLastName:EditText
    lateinit var edtEmail:EditText

    lateinit var tvUserName:TextView
    lateinit var tvEmail:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setDetailToolbar(toolbar)
        title = getString(R.string.lbl_edit_profile)
       // mAppBarColor()
        //changeColor()
        ivProfile = findViewById(R.id.ivProfile)
        tvCountWishList = findViewById(R.id.tvCountWishList)
        tvOrderCount = findViewById(R.id.tvOrderCount)
        cbCheck = findViewById(R.id.cbCheck)

        edtShippingFName = findViewById(R.id.edtShippingFName)
        edtShippingLName = findViewById(R.id.edtShippingLName)
        edtShippingCompany = findViewById(R.id.edtShippingCompany)
        edtShippingAdd1 = findViewById(R.id.edtShippingAdd1)
        edtShippingAdd2 = findViewById(R.id.edtShippingAdd2)
        edtShippingCity = findViewById(R.id.edtShippingCity)
        edtShippingPinCode = findViewById(R.id.edtShippingPinCode)

        edtBillingFName = findViewById(R.id.edtBillingFName)
        edtBillingLName = findViewById(R.id.edtBillingLName)
        edtBillingCompany = findViewById(R.id.edtBillingCompany)
        edtBillingAdd1 = findViewById(R.id.edtBillingAdd1)
        edtBillingAdd2 = findViewById(R.id.edtBillingAdd2)
        edtBillingCity = findViewById(R.id.edtBillingCity)
        edtBillingPinCode = findViewById(R.id.edtBillingPinCode)


        edtBillingPhone = findViewById(R.id.edtBillingPhone)
        edtBillingEmail = findViewById(R.id.edtBillingEmail)

        btnSaveProFile = findViewById(R.id.btnSaveProFile)
        editProfileImage = findViewById(R.id.editProfileImage)
        llOrder = findViewById(R.id.llOrder)
        llWishList = findViewById(R.id.llWishList)
        edtFirstName = findViewById(R.id.edtFirstName)
        edtLastName = findViewById(R.id.edtLastName)
        edtEmail = findViewById(R.id.edtEmail)

        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tvEmail)



       // ivProfile.loadImageFromUrl(getUserProfile(), aPlaceHolderImage = R.drawable.ic_profile)

        tvCountWishList.text = getWishListCount()
        tvOrderCount.text = getOrderCount()

        cbCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edtShippingFName.setText(edtBillingFName.text.toString())
                edtShippingLName.setText(edtBillingLName.text.toString())
                edtShippingCompany.setText(edtBillingCompany.text.toString())
                edtShippingAdd1.setText(edtBillingAdd1.text.toString())
                edtShippingAdd2.setText(edtBillingAdd2.text.toString())
                edtShippingCity.setText(edtBillingCity.text.toString())
                edtShippingPinCode.setText(edtBillingPinCode.text.toString())

            } else {
                edtShippingFName.text.clear()
                edtShippingLName.text.clear()
                edtShippingCompany.text.clear()
                edtShippingAdd1.text.clear()
                edtShippingAdd2.text.clear()
                edtShippingCity.text.clear()
                edtShippingPinCode.text.clear()
            }
        }

        getData()
        setUpListener()
    }

    private fun setUpListener() {
        btnSaveProFile.setOnClickListener {
            if (validate()) {
                updateProfile()
            }
        }
        editProfileImage.setOnClickListener {
        }
        llOrder.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
        llWishList.setOnClickListener {
            startActivity(Intent(this,WishlistActivity::class.java))
        }
    }

    private fun updateProfile() {

        val mBilling = BillingAddress()
        mBilling.firstName = edtBillingFName.textToString()
        mBilling.lastName = edtBillingLName.textToString()
        mBilling.address1 = edtBillingAdd1.textToString()
        mBilling.address2 = edtBillingAdd2.textToString()
        mBilling.city = edtBillingCity.textToString()
        mBilling.company = edtBillingCompany.textToString()
        mBilling.postcode = edtBillingPinCode.textToString()

        mBilling.country = "morocco"

        mBilling.phone = edtBillingPhone.textToString()
        mBilling.email = edtBillingEmail.textToString()

        val mShipping = ShippingAddress()
        mShipping.firstName = edtShippingFName.textToString()
        mShipping.lastName = edtShippingLName.textToString()
        mShipping.address1 = edtShippingAdd1.textToString()
        mShipping.company = edtShippingCompany.textToString()
        mShipping.address2 = edtShippingAdd2.textToString()
        mShipping.city = edtShippingCity.textToString()
        mShipping.postcode = edtShippingPinCode.textToString()

        mShipping.country = "morocco"

        val customer = Customer()
        customer.email = edtEmail.textToString()
        customer.firstName = edtFirstName.textToString()
        customer.lastName = edtLastName.textToString()
        customer.billingAddress = mBilling
        customer.shippingAddress = mShipping
        // TODO: 27/01/2021 add new customer

        Toast.makeText(this,getString(R.string.lbl_profile_saved_successfully),Toast.LENGTH_SHORT).show()
        setCustomer(customer)

        setResult(Activity.RESULT_OK)
        finish()

    }
    fun setCustomer(customer: Customer){
        getSharedPrefInstance().setValue(Constants.SharedPref.BILLING, Gson().toJson(customer.billingAddress))
        getSharedPrefInstance().setValue(Constants.SharedPref.SHIPPING, Gson().toJson(customer.shippingAddress))
        getSharedPrefInstance().setValue(Constants.SharedPref.USER_FIRST_NAME, customer.firstName)
        getSharedPrefInstance().setValue(Constants.SharedPref.USER_LAST_NAME, customer.lastName)
        getSharedPrefInstance().setValue(Constants.SharedPref.USER_EMAIL,customer.email)
        getSharedPrefInstance().setValue(Constants.SharedPref.USER_ROLE, "customer")
        getSharedPrefInstance().setValue(Constants.SharedPref.USER_PICODE, customer.shippingAddress?.postcode)
    }
    fun getCustomer(): Customer {
        val customer= Customer()
        customer.shippingAddress = getShippingList()
        customer.billingAddress = getbillingList()
        customer.firstName = getSharedPrefInstance().getStringValue(Constants.SharedPref.USER_FIRST_NAME)
        customer.lastName = getSharedPrefInstance().getStringValue(Constants.SharedPref.USER_LAST_NAME)
        customer.role = getSharedPrefInstance().getStringValue(Constants.SharedPref.USER_ROLE)
        customer.email = getEmail()
        return customer
    }
    private fun getData() {
        val customer = getCustomer()

        getSharedPrefInstance().setValue(Constants.SharedPref.SHOW_SWIPE, true)
        edtFirstName.setText(customer.firstName)
        edtLastName.setText(customer.lastName)

        tvUserName.text = customer.username
        tvEmail.text = customer.email
        edtEmail.setText(customer.email)

        edtFirstName.setSelection(edtFirstName.text.length)

        edtBillingFName.setText(customer.billingAddress?.firstName)
        edtBillingLName.setText(customer.billingAddress?.lastName)
        edtBillingAdd1.setText(customer.billingAddress?.address1)
        edtBillingAdd2.setText(customer.billingAddress?.address2)
        edtBillingCompany.setText(customer.billingAddress?.company)
        edtBillingCity.setText(customer.billingAddress?.city)
        edtBillingPinCode.setText(customer.billingAddress?.postcode)
        edtBillingPhone.setText(customer.billingAddress?.phone)
        edtBillingEmail.setText(customer.billingAddress?.email)

        edtShippingFName.setText(customer.shippingAddress?.firstName)
        edtShippingLName.setText(customer.shippingAddress?.lastName)
        edtShippingAdd1.setText(customer.shippingAddress?.address1)
        edtShippingCompany.setText(customer.shippingAddress?.company)
        edtShippingAdd2.setText(customer.shippingAddress?.address2)
        edtShippingCity.setText(customer.shippingAddress?.city)
        edtShippingPinCode.setText(customer.shippingAddress?.postcode)

    }


    private fun validate(): Boolean {
        return when {
            edtFirstName.checkIsEmpty() -> {
                edtFirstName.showError(getString(R.string.error_field_required))
                false
            }
            !edtFirstName.isValidText() -> {
                edtFirstName.showError(getString(R.string.error_validText))
                false
            }
            edtLastName.checkIsEmpty() -> {
                edtLastName.showError(getString(R.string.error_field_required))
                false
            }
            !edtLastName.isValidText() -> {
                edtLastName.showError(getString(R.string.error_validText))
                false
            }
            edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.error_field_required))
                false
            }
            !edtEmail.isValidEmail() -> {
                edtEmail.showError(getString(R.string.error_enter_valid_email))
                false
            }
            edtShippingFName.checkIsEmpty() -> {
                edtShippingFName.showError(getString(R.string.error_field_required))
                false
            }
            !edtShippingFName.isValidText() -> {
                edtShippingFName.showError(getString(R.string.error_validText))
                false
            }
            edtShippingLName.checkIsEmpty() -> {
                edtShippingLName.showError(getString(R.string.error_field_required))
                false
            }
            !edtShippingLName.isValidText() -> {
                edtShippingLName.showError(getString(R.string.error_validText))
                false
            }

            edtShippingPinCode.checkIsEmpty() -> {
                edtShippingPinCode.showError(getString(R.string.error_field_required))
                false
            }

            edtShippingAdd1.checkIsEmpty() -> {
                edtShippingAdd1.showError(getString(R.string.error_field_required))
                false
            }
            edtShippingAdd2.checkIsEmpty() -> {
                edtShippingAdd2.showError(getString(R.string.error_field_required))
                false
            }
            edtShippingCity.checkIsEmpty() -> {
                edtShippingCity.showError(getString(R.string.error_field_required))
                false
            }

            edtBillingFName.checkIsEmpty() -> {
                edtBillingFName.showError(getString(R.string.error_field_required))
                false
            }
            !edtBillingFName.isValidText() -> {
                edtBillingFName.showError(getString(R.string.error_validText))
                false
            }
            edtBillingLName.checkIsEmpty() -> {
                edtBillingLName.showError(getString(R.string.error_field_required))
                false
            }
            !edtBillingLName.isValidText() -> {
                edtBillingLName.showError(getString(R.string.error_validText))
                false
            }
            edtBillingAdd1.checkIsEmpty() -> {
                edtBillingAdd1.showError(getString(R.string.error_field_required))
                false
            }
            edtBillingAdd2.checkIsEmpty() -> {
                edtBillingAdd2.showError(getString(R.string.error_field_required))
                false
            }
            edtBillingCity.checkIsEmpty() -> {
                edtBillingCity.showError(getString(R.string.error_field_required))
                false
            }
            edtBillingPinCode.checkIsEmpty() -> {
                edtBillingPinCode.showError(getString(R.string.error_field_required))
                false
            }
            edtBillingPhone.checkIsEmpty() -> {
                edtBillingPhone.showError(getString(R.string.error_field_required))
                false
            }
            edtBillingEmail.checkIsEmpty() -> {
                edtBillingEmail.showError(getString(R.string.error_field_required))
                false
            }
            !edtBillingEmail.isValidEmail() -> {
                edtBillingEmail.showError(getString(R.string.error_enter_valid_email))
                false
            }
            else -> true
        }

    }
 /*   private fun changeColor() {
        tvUserName.changeTextPrimaryColor()
        edtFirstName.changeTextSecondaryColor()
        edtLastName.changeTextSecondaryColor()
        edtEmail.changeTextSecondaryColor()
        edtBillingPhone.changeTextSecondaryColor()
        edtBillingEmail.changeTextSecondaryColor()
        cbCheck.changeTextPrimaryColor()
        lblOrder.changeTextPrimaryColor()
        tvCountWishList.changePrimaryColor()
        lblWishList.changeTextPrimaryColor()
        tvOrderCount.changePrimaryColor()
        lblPersonal.changeTextPrimaryColor()
        lblPersonal.changeTextPrimaryColor()
        lblBilling.changeTextPrimaryColor()
        lblShipping.changeTextPrimaryColor()
        edtShippingFName.changeTextSecondaryColor()
        edtBillingFName.changeTextSecondaryColor()
        edtShippingLName.changeTextSecondaryColor()
        edtBillingLName.changeTextSecondaryColor()
        edtShippingCompany.changeTextSecondaryColor()
        edtBillingCompany.changeTextSecondaryColor()
        edtShippingAdd1.changeTextSecondaryColor()
        edtBillingAdd1.changeTextSecondaryColor()
        edtShippingAdd2.changeTextSecondaryColor()
        edtBillingAdd2.changeTextSecondaryColor()
        edtShippingCity.changeTextSecondaryColor()
        edtBillingCity.changeTextSecondaryColor()
        edtShippingPinCode.changeTextSecondaryColor()
        edtBillingPinCode.changeTextSecondaryColor()
        rlMain.changeBackgroundColor()
        tvEmail.changePrimaryColor()
        btnSaveProFile.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(getButtonColor()))
    }*/
}

