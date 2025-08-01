package ma.pam.ajitsowak.ui.activity

import androidx.appcompat.app.AppCompatActivity

class CouponActivity : AppCompatActivity() {
    /*
    private var couponsAdapter: BaseAdapter<Coupons>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)

        setDetailToolbar(toolbar)
        title = getString(R.string.lbl_available_coupon)
        mAppBarColor()
        changeColor()
        couponsAdapter =
            BaseAdapter(R.layout.item_coupon, onBind = { view, item, _ ->
                when (item.discount_type) {
                    "percent" -> {
                        view.tvCouponName.text =
                            getString(R.string.lbl_get) + item.amount + getString(R.string.lbl_off)

                    }
                    "fixed_cart" -> {
                        view.tvCouponName.text =
                            getString(R.string.lbl_get_flat) + item.amount.currencyFormat() + " " + getString(
                                R.string.lbl_off1
                            )
                    }
                    "fixed_product" -> {
                        view.tvCouponName.text =
                            getString(R.string.lbl_get_flat) + item.amount.currencyFormat() + " " + getString(
                                R.string.lbl_off1
                            ) + " " + getString(R.string.lbl_to_all_product)
                    }
                    else -> {
                        view.tvCouponName.text =
                            getString(R.string.lbl_get) + item.amount.currencyFormat() + getString(R.string.lbl_off1)
                    }
                }

                view.tvInfo.text = item.description
                view.tvCouponOffer.text = item.code.toUpperCase()

                if (item.minimum_amount.toFloat() > 0) {
                    view.tvMinAmountInfo.text =
                        getString(R.string.lbl_valid_only_orders_of) + item.minimum_amount.currencyFormat() + getString(
                            R.string.lbl_and_above
                        )
                    if (item.maximum_amount.toFloat() > 0) {
                        val finalString = view.tvMinAmountInfo.text.toString()
                        view.tvMinAmountInfo.text =
                            finalString + "\nMaximum bill amount is " + item.maximum_amount.currencyFormat()
                    }
                } else if (item.maximum_amount.toFloat() > 0) {
                    val finalString = view.tvMinAmountInfo.text.toString()
                    view.tvMinAmountInfo.text =
                        finalString + "\nMaximum bill amount is " + item.maximum_amount.currencyFormat()
                } else {
                    if (item.usage_limit > 0) {
                        getString(R.string.lbl_valid_only_for_first) + item.usage_limit + getString(
                            R.string.lbl_users
                        )
                    } else {
                        view.tvMinAmountInfo.text =
                            getString(R.string.lbl_no_minimum_order_value_needed)
                    }
                }
                if(item.date_expires!=null)
                {
                    if (!item.date_expires.isBlank()) {
                        val dateFormat = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss"
                        )
                        val endDate: Date = dateFormat.parse(item.date_expires)
                        val postFormater =
                            SimpleDateFormat("MMM dd, yyyy")
                        val newDateStr = postFormater.format(endDate)
                        view.lblValidTill.text = getString(R.string.lbl_valid_till) + " " + newDateStr
                        val current = Date()
                        if (endDate.before(current)) {
                            view.lblValidTill.text = ""
                            view.tvApply.visibility = View.GONE
                        } else {
                            view.lblValidTill.text =
                                getString(R.string.lbl_valid_till) + " " + newDateStr
                            view.tvApply.visibility = View.VISIBLE
                        }
                    }
                }
                else if (item.usage_limit > 0) {
                    if (item.usage_limit == item.usage_count) {
                        view.lblValidTill.text = ""
                        view.tvApply.visibility = View.GONE
                    } else {
                        view.tvApply.visibility = View.VISIBLE
                    }
                }
                view.tvApply.onClick {
                    val returnIntent = Intent()
                    returnIntent.putExtra("couponData", item)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }

                val shareText =
                    view.tvCouponName.text.toString() + "\n\nApply Coupon Code: " + item.code.toUpperCase() +
                            "\n\nDownload app from here: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID

                view.imgShare.onClick {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        shareText
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }

                view.tvCouponName.changeTextPrimaryColor()
                view.tvInfo.changeTextSecondaryColor()
                view.tvMinAmountInfo.changeTextSecondaryColor()
                view.tvCouponOffer.changeAccentColor()
                view.lblValidTill.changeTextSecondaryColor()
                view.tvApply.changeAccentColor()
            })

        rvCoupons.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCoupons.adapter = couponsAdapter
        listCoupons {
            couponsAdapter!!.clearItems()
            couponsAdapter!!.addItems(it)
            couponsAdapter!!.notifyDataSetChanged()
        }
        rvCoupons.rvItemAnimation()
    }

    private fun changeColor(){
        llMain.changeBackgroundColor()
    }
*/
}
