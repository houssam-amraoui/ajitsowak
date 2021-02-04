package ma.pam.ajitsowak.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.Constants
import ma.pam.ajitsowak.utils.convertToLocalDate
import ma.pam.ajitsowak.utils.currencyFormat
import ma.pam.ajitsowak.woolib.models.Order
import ma.pam.ajitsowak.woolib.models.OrderNote


class PaymentSuccessfullyActivity : AppCompatActivity() {

    lateinit var amount:TextView
    lateinit var transactionId:TextView
    lateinit var orderId:TextView
    lateinit var paidVia:TextView
    lateinit var transactionDate:TextView
    lateinit var imgBack:ImageView
    lateinit var tvDone:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_transaction)

        amount = findViewById(R.id.amount)
        transactionId = findViewById(R.id.transactionId)
        orderId = findViewById(R.id.orderId)
        paidVia = findViewById(R.id.paidVia)
        transactionDate = findViewById(R.id.transactionDate)
        imgBack = findViewById(R.id.imgBack)
        tvDone = findViewById(R.id.tvDone)



        //changeColor()
        val response: Order = intent.getSerializableExtra(Constants.KeyIntent.ORDER_DATA) as Order


        amount.text = response.total.currencyFormat()
        transactionId.text = response.transaction_id
        orderId.text = response.order_key
        paidVia.text = response.payment_method
        transactionDate.text = convertToLocalDate(response.date_created)

        imgBack.setOnClickListener{
            finish()
        }

        tvDone.setOnClickListener {
            finish()
        }

        //Place Order Notes

        val notes = OrderNote()
        notes.isCustomer_note = true
        notes.note = "{\n" +
                "\"status\":\"Ordered\",\n" +
                "\"message\":\"Your order has been placed.\"\n" +
                "} "

        Toast.makeText(this,"make not",Toast.LENGTH_LONG).show()

        /*
        getRestApiImpl().addOrderNotes(response.id, request = notes, onApiSuccess = {
        }, onApiError = {
        })*/

    }

   /* private fun changeColor() {
        lblOrderPlaceSuccessfully.changeTextPrimaryColor()
        lblTotalAmount.changeTextSecondaryColor()
        amount.changeTextPrimaryColor()
        lblTransaction.changeTextSecondaryColor()
        transactionId.changeTextPrimaryColor()
        lblOrderId.changeTextSecondaryColor()
        orderId.changeTextPrimaryColor()
        lblPaymentThrough.changeTextSecondaryColor()
        paidVia.changeTextPrimaryColor()
        lblTransactionFee.changeTextSecondaryColor()
        transactionDate.changeTextPrimaryColor()
        tvDone.changeBackgroundTint(getButtonColor())
        rlMain.changeBackgroundColor()
    }*/


}
