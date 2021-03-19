package ma.pam.ajitsowak.woolib.repo

import android.util.Log
import com.google.gson.GsonBuilder
import ma.pam.ajitsowak.woolib.data.auth.OAuthInterceptor
import ma.pam.ajitsowak.MyApp.getAppInstance
import ma.pam.ajitsowak.utils.Constants.SharedPref.CONSUMERKEY
import ma.pam.ajitsowak.utils.Constants.SharedPref.CONSUMERSECRET
import ma.pam.ajitsowak.utils.getSharedPrefInstance

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

import java.util.concurrent.TimeUnit

open class WooRepository(baseUrl: String, consumerKey: String, consumerSecret: String) {
    var retrofit: Retrofit
    init {
        val cacheSize = 50 * 1024
        val cache = Cache(File(getAppInstance().cacheDir, "responses"), cacheSize.toLong())

        val oauth1WooCommerce = OAuthInterceptor.Builder()
                .consumerKey(consumerKey)
                .consumerSecret(consumerSecret)
                .build()

       /* val oauth1WooCommerce = OAuthInterceptor.Builder()
                .consumerKey(getSharedPrefInstance().getStringValue(CONSUMERKEY))
                .consumerSecret(getSharedPrefInstance().getStringValue(CONSUMERSECRET))
                .build()*/

        val builder = OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.MINUTES).readTimeout(3, TimeUnit.MINUTES)

        val okHttpClient = builder
                .addInterceptor(ResponseInterceptor())
                .addInterceptor(UnauthorizedInterceptor())
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val request: Request =
                                chain.request().newBuilder().addHeader(
                                        "consumerKey",
                                        getSharedPrefInstance().getStringValue(CONSUMERKEY)
                                ).addHeader(
                                        "consumerSecret",
                                        getSharedPrefInstance().getStringValue(CONSUMERSECRET)
                                ).build()
                        return chain.proceed(request)
                    }
                })
                .addInterceptor(oauth1WooCommerce)
                .cache(cache)
                .build()

      /*  val gson = GsonBuilder()
                .setLenient()
                .disableHtmlEscaping()
                .create()

         Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
*/
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .disableHtmlEscaping()
                .create()

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

    }

}
/*
class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())


        return response.newBuilder()
                .body(
                        ResponseBody.create(
                                "application/json; charset=utf-8".toMediaTypeOrNull(),
                                response.body!!.bytes()
                        )
                )
                .build()


    }
}
*/

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
                .body(
                        response.body!!.bytes().toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
                )
                .build()
    }
}

internal class UnauthorizedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        Log.e("ddad", response.body.toString())
        return response
    }
}
