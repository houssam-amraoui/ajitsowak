package ma.pam.ajitsowak.woolib.repo.order

import ma.pam.ajitsowak.woolib.data.api.OrderNoteAPI
import ma.pam.ajitsowak.woolib.models.Order
import ma.pam.ajitsowak.woolib.models.OrderNote
import ma.pam.ajitsowak.woolib.models.filters.OrderNoteFilter
import ma.pam.ajitsowak.woolib.repo.WooRepository
import retrofit2.Call

class OrderNoteRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: OrderNoteAPI = retrofit.create(OrderNoteAPI::class.java)

    fun create(id: Int, note: OrderNote): Call<OrderNote> {
        return apiService.create(id, note)
    }

    fun note(idOreder: Int, idNote: Int): Call<OrderNote> {
        return apiService.view(idOreder, idNote)
    }

    fun notes(id: Int): Call<List<OrderNote>> {
        return apiService.list(id)
    }

    fun notes(order: Order, orderNoteFilter: OrderNoteFilter): Call<List<OrderNote>> {
        return apiService.filter(order.id, orderNoteFilter.filters)
    }

    fun delete(order: Order, id: Int): Call<OrderNote> {
        return apiService.delete(order.id, id)
    }

    fun delete(order: Order, id: Int, force: Boolean): Call<OrderNote> {
        return apiService.delete(order.id, id, force)
    }


}
