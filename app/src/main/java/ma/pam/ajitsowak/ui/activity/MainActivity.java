package ma.pam.ajitsowak.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ma.pam.ajitsowak.R;
import ma.pam.ajitsowak.api.WooApi;
import ma.pam.ajitsowak.woolib.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public WooApi wooapi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        wooapi = WooApi.getInstance();

        wooapi.getInstance().getProductDetail(10).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product rr= response.body();

                if (rr.getStock_status().equals("instock"))
                    Toast.makeText(MainActivity.this, "niihaha", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }});

    }



}
