package ma.pam.ajitsowak.api;

import java.util.List;
import java.util.Random;

import ma.pam.ajitsowak.Config;
import ma.pam.ajitsowak.woolib.Woocommerce;

import ma.pam.ajitsowak.woolib.models.Category;
import ma.pam.ajitsowak.woolib.models.LineItem;
import ma.pam.ajitsowak.woolib.models.Order;
import ma.pam.ajitsowak.woolib.models.OrderData;
import ma.pam.ajitsowak.woolib.models.OrderNote;
import ma.pam.ajitsowak.woolib.models.Product;
import ma.pam.ajitsowak.woolib.models.ShippingMethod;
import ma.pam.ajitsowak.woolib.models.filters.OrderFilter;
import ma.pam.ajitsowak.woolib.models.filters.ProductCategoryFilter;
import ma.pam.ajitsowak.woolib.models.filters.ProductFilter;
import ma.pam.ajitsowak.woolib.models.filters.Sort;
import retrofit2.Call;

public class WooApi {
    private static WooApi wooApi;
    private final Woocommerce woocommerce;

    public static WooApi getInstance() {


        if (wooApi == null)
            wooApi =  new WooApi();

        return wooApi;
    }

    private WooApi() {
        woocommerce = Woocommerce.Builder()
                .setSiteUrl(Config.siteUrl)
                .setApiVersion(Woocommerce.API_V3)
                .setConsumerKey(Config.consumerKey)
                .setConsumerSecret(Config.consumerSecret)
                .build();
    }

    public Call<List<Product>> getAllProduct(int page, int count){
        return woocommerce.ProductRepository().products(page, count);
    }

    public Call<List<Product>> getFilterProduct(ProductFilter filter){

        return woocommerce.ProductRepository().products(filter);
    }

    public Call<List<Product>> getNewProducts(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setOrderby("date");
        productFilter.setOrder(Sort.DESCENDING);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> getPopularProducts(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setOrderby("popularity");
        productFilter.setOrder(Sort.DESCENDING);
        return woocommerce.ProductRepository().products(productFilter);
    }
    public Call<List<Product>> getFeaturedProduct(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setFeatured(true);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> getMostRateProduct(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setOrderby("rating");
        productFilter.setOrder(Sort.DESCENDING);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> getRandomProduct(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        String[] arg = {"rating","popularity","date"};
        int rand = new Random().nextInt(arg.length);
        productFilter.setOrderby(arg[rand]);
        productFilter.setOrder(Sort.ASCENDING);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> getOnSaleProduct(int page, int count){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setOn_sale(true);
        productFilter.setOrder(Sort.DESCENDING);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> getProductByCategory(int page, int count, int idCategory){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setPage(page);
        productFilter.setPer_page(count);
        productFilter.setCategory(idCategory);

        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Category>> getAllCategory(int page, int count){
        ProductCategoryFilter categoryFilter = new ProductCategoryFilter();
        categoryFilter.setPage(page);
        categoryFilter.setPer_page(count);
        return woocommerce.CategoryRepository().categories(categoryFilter);
    }

    public Call<List<Category>> getCheldOfCategory(int page, int count, int idCategory){
        ProductCategoryFilter categoryFilter = new ProductCategoryFilter();
        categoryFilter.setPage(page);
        categoryFilter.setPer_page(count);
        categoryFilter.setParent(new int[]{idCategory});
        return woocommerce.CategoryRepository().categories(categoryFilter);
    }

    public Call<Product> getProductDetail(int product){
        return woocommerce.ProductRepository().product(product);
    }

    public Call<List<Product>> getProductIncludeId(int[] product){
        ProductFilter productFilter = new ProductFilter();
        productFilter.setInclude(product);
        return woocommerce.ProductRepository().products(productFilter);
    }

    public Call<List<Product>> test(int[] product){
        Order aa;
        OrderData bb;
        LineItem vv;
        ShippingMethod eze ;

       // woocommerce.ShippingMethodRepository().retrofit.create(ShippingZoneAPI.class).view()

        return woocommerce.ProductRepository().products();
    }

    public Call<Order> createOreder(Order order){
        return woocommerce.OrderRepository().create(order);
    }
    
    public Call<OrderNote> addOrderNotes(int id, OrderNote orderNote){
        return woocommerce.OrderNoteRepository().create(id,orderNote);
    }

    public Call<List<OrderNote>> getOrderNotes(int idOreder){
        return woocommerce.OrderNoteRepository().notes(idOreder);
    }

    public Call<OrderNote> deleteOrder(Order order){
        // TODO: 31/01/2021 just false test 
        return woocommerce.OrderNoteRepository().delete(order,0);
    }

    public Call<List<Order>> getOrderIncludeId(int[] orders){
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setInclude(orders);
        return woocommerce.OrderRepository().orders(orderFilter);
    }

    public Call<Order> updateOreder(int id,Order order){
        return woocommerce.OrderRepository().update(id,order);
    }
}
