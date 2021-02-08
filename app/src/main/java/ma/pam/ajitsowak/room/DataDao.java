package ma.pam.ajitsowak.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long addToCart(CartItem cartItem);

    @Query("select * from CartItem")
    List<CartItem> getCartList();

    @Delete
    int DeleteCart(CartItem cartItem);

    @Update
    void UpdateCart(CartItem cartItem);

    @Query("DELETE FROM CartItem")
    void DeleteAllCart();

    @Query("select count(productId) from CartItem")
    int getCartCount();

    @Query("SELECT EXISTS(select * from CartItem where productId = :id)")
    Boolean isCartAdded(int id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFav(FavModel favModel);
    @Delete
    void deleteFav(FavModel favModel);
    @Query("select * from FavModel")
    List<FavModel> getallFavs();
    @Query("SELECT EXISTS(select * from FavModel where idProduct = :id)")
    Boolean isFavsAdded(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOrder(OrderItem orderItem);

    @Query("select * from orderItem")
    List<OrderItem> getOrderList();


  /*  private fun setUpPage() {
        var itemCount = cartItems.size
        var total = 0

        for (cartitem in cartItems){
            var price = if (cartitem.product.isOn_sale) {
                cartitem.product.sale_price.replace(",","").toInt()
            }else{
                cartitem.product.regular_price.replace(",","").toInt()
            }

            var qty = cartitem.quantity

            total += price * qty

        }

        if (itemCount == 1){
            tvTotalItemCountTitle.text = "Item ($itemCount)"
        }else{
            tvTotalItemCountTitle.text = "Items ($itemCount)"
        }

        tvTotalItemCost.text = "Ksh$total"
        tvTotal.text = "Ksh$total"

        flSave.setOnClickListener{
            prepOrder()
        }
    }

    private fun prepOrder() {
        var order = Order()

        var lineitems  = ArrayList<LineItem>()

        for (cartitem in cartItems){
            var lineItem = LineItem()
            lineItem.price = cartitem.getPrice().toString()
            lineItem.productId = cartitem.productId
            lineItem.quantity = cartitem.quantity

            lineitems.add(lineItem)
        }

        order.lineItems = lineitems
        order.billingAddress = customer.billingAddress
        order.shippingAddress = customer.shippingAddress
        order.customer = customer

        createOrder(order)
    }*/

}
