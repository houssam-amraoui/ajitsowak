package ma.pam.ajitsowak.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.room.FavModel
import ma.pam.ajitsowak.ui.activity.ProductDetailActivity1
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.KeyIntent.DATA
import ma.pam.ajitsowak.woolib.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WishListFragment : Fragment() {

    lateinit var rvWishList:RecyclerView
    lateinit var rlNoData:View

    private val mListAdapter = BaseAdapter<Product>(R.layout.item_wishlist, onBind = { view, model, _ ->
        if (activity !== null) {
            if(model.sale_price.isNotEmpty()){
                view.findViewById<TextView>(R.id.tvOriginalPrice).applyStrike()
                view.findViewById<TextView>(R.id.tvDiscountPrice).text = model.sale_price.currencyFormat()
                view.findViewById<TextView>(R.id.tvOriginalPrice).text = model.regular_price.currencyFormat()
            }
            else
            {
                if (model.regular_price.isEmpty()) {
                    view.findViewById<TextView>(R.id.tvDiscountPrice).text =model.price.currencyFormat()
                } else {
                    view.findViewById<TextView>(R.id.tvDiscountPrice).text =model.regular_price.currencyFormat()
                }
            }
            view.findViewById<TextView>(R.id.tvProductName).text = model.name
           // view.findViewById<TextView>(R.id.tvProductName).changeTextPrimaryColor()
           // view.findViewById<TextView>(R.id.tvDiscountPrice).changeTextPrimaryColor()
           // view.findViewById<TextView>(R.id.tvOriginalPrice).changeTextSecondaryColor()
            view.findViewById<ImageView>(R.id.ivProduct).loadImageFromUrl(model.images[0].src!!)
        }
        view.setOnClickListener {
            startActivity(Intent(activity, ProductDetailActivity1::class.java).apply {
                putExtra(DATA, model)
            })
        }
        /*view.ivMoveToCart.onClick {
            val requestModel = RequestModel()
            requestModel.pro_id = model.pro_id
            requestModel.quantity = 1
            activity?.setResult(Activity.RESULT_OK)
            getRestApiImpl().addItemToCart(request = requestModel, onApiSuccess = {
                if (activity == null) return@addItemToCart
                snackBar(getString(R.string.success_add))
                activity?.sendCartBroadcast()
                activity!!.fetchAndStoreCartData()
                (activity as AppBaseActivity).removeFromWishList(requestModel)
                {
                    if (it) snackBar(getString(R.string.lbl_remove)); hideProgress()
                    wishListItemChange()
                }
            }, onApiError = {
                if (activity == null) return@addItemToCart
                snackBar(it)
                (activity as AppBaseActivity).removeFromWishList(requestModel)
                {
                    if (it) snackBar(getString(R.string.lbl_remove)); hideProgress()
                    wishListItemChange()
                }
                (activity as AppBaseActivity).fetchAndStoreCartData()

            })
        }*/
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_wishlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvWishList =  view.findViewById(R.id.rvWishList)
        rlNoData = view.findViewById(R.id.rlNoData)

        rvWishList.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = mListAdapter
            setHasFixedSize(true)
            rvItemAnimation()

        }
        //llMain.changeBackgroundColor()
    }

    override fun onResume() {
        super.onResume()
        wishListItemChange()
    }

    private fun wishListItemChange() {
        val fav:List<FavModel> = getRoom().Dao().getallFavs()
        if (fav.isNullOrEmpty()) {
            rvWishList.hide()
            rlNoData.show()
        } else {
            rlNoData.hide()
            rvWishList.show()
            mListAdapter.clearItems()
            val temp= IntArray(fav.size)
            fav.forEachIndexed { index, favModel ->
                temp[index]=favModel.idProduct
            }
            getWooApi().getProductIncludeId(temp).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    mListAdapter.addItems(response.body()!!)
                }
            })

        }

        /*
        if (activity == null) return

        getSharedPrefInstance().setValue(Constants.SharedPref.KEY_WISHLIST_COUNT, it.size)
        */

    }

}
