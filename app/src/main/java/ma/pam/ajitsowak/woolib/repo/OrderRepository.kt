package ma.pam.ajitsowak.woolib.repo

import ma.pam.ajitsowak.woolib.data.api.OrderAPI
import ma.pam.ajitsowak.woolib.models.LineItem
import ma.pam.ajitsowak.woolib.models.Order
import ma.pam.ajitsowak.woolib.models.OrderNote
import ma.pam.ajitsowak.woolib.models.filters.OrderFilter
import ma.pam.ajitsowak.woolib.repo.order.OrderNoteRepository
import ma.pam.ajitsowak.woolib.repo.order.RefundRepository
import retrofit2.Call

class OrderRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: OrderAPI

    internal var orderNoteRepository: OrderNoteRepository
    internal var refundRepository: RefundRepository

    init {
        apiService = retrofit.create(OrderAPI::class.java)

        orderNoteRepository = OrderNoteRepository(baseUrl, consumerKey, consumerSecret)
        refundRepository = RefundRepository(baseUrl, consumerKey, consumerSecret)
    }

    fun create(order: Order): Call<Order> {
        return apiService.create(order)
    }

    fun addToCart(productId: Int, cartOrder: Order?): Call<Order> {
        var cartOrder = cartOrder
        val lineItem = LineItem()
        lineItem.product_id = productId
        lineItem.quantity = 1

        if (cartOrder != null) {
            cartOrder.addLineItem(lineItem)
            return apiService.update(cartOrder.id, cartOrder)
        } else {
            cartOrder = Order()
            cartOrder.orderNumber = "Cart"
            cartOrder.status = "on-hold"
            cartOrder.addLineItem(lineItem)
            return apiService.create(cartOrder)
        }

    }

    fun cart(): Call<List<Order>> {
        val orderFilter = OrderFilter()
        orderFilter.status = "on-hold"

        return apiService.filter(orderFilter.filters)
    }

    fun order(id: Int): Call<Order> {
        return apiService.view(id)
    }

    fun orders(): Call<List<Order>> {
        return apiService.list()
    }

    fun orders(orderFilter: OrderFilter): Call<List<Order>> {
        return apiService.filter(orderFilter.filters)
    }

    fun update(id: Int, order: Order): Call<Order> {
        return apiService.update(id, order)
    }

    fun delete(id: Int): Call<Order> {
        return apiService.delete(id)
    }

    fun delete(id: Int, force: Boolean): Call<Order> {
        return apiService.delete(id, force)
    }


    fun createNote(id: Int, note: OrderNote): Call<OrderNote> {
        return orderNoteRepository.create(id, note)
    }

    fun note(idOreder: Int, id: Int): Call<OrderNote> {
        return orderNoteRepository.note(idOreder, id)
    }

    fun notes(id: Int): Call<List<OrderNote>> {
        return orderNoteRepository.notes(id)
    }

    fun deleteNote(order: Order, id: Int): Call<OrderNote> {
        return orderNoteRepository.delete(order, id)
    }

    fun deleteNote(order: Order, id: Int, force: Boolean): Call<OrderNote> {
        return orderNoteRepository.delete(order, id, force)
    }


}
