package ma.pam.ajitsowak.ui.activity

import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.Constants
import ma.pam.ajitsowak.utils.setDetailToolbar

class WebViewActivity : mAppCompatActivity() {
    var mIsError = false
    private var mWebViewClient = WebViewClient()
    private var webChromeClient = WebChromeClient()

    lateinit var toolbar:Toolbar
    lateinit var webView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_payment)

        toolbar = findViewById(R.id.toolbar)
        setDetailToolbar(toolbar)

        webView = findViewById(R.id.webView)

      //  mAppBarColor()
        setupWebView()
        title = getString(R.string.lbl_payment)

        webView.loadUrl(intent.getStringExtra(Constants.KeyIntent.CHECKOUT_URL)!!)


    }

    private fun setupWebView() {
       // showProgress(true)
        configureWebView()
        configureWebClient()
        initChromeClient()
    }

    private fun initChromeClient() {
        var customView: View? = null
        var customViewCallback: WebChromeClient.CustomViewCallback? = null
        var originalOrientation = 0
        var originalSystemUiVisibility = 0

        webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                toolbar.title = title
            }

            override fun onShowCustomView(
                paramView: View?,
                paramCustomViewCallback: CustomViewCallback
            ) {
                if (customView != null) {
                    onHideCustomView()
                    return
                }
                customView = paramView
                originalSystemUiVisibility = window.decorView.systemUiVisibility
                originalOrientation = requestedOrientation
                customViewCallback = paramCustomViewCallback
                (window.decorView as FrameLayout).addView(
                    customView,
                    FrameLayout.LayoutParams(-1, -1)
                )
                window.decorView.systemUiVisibility = 3846
            }

            override fun onHideCustomView() {
                (window.decorView as FrameLayout).removeView(customView)
                customView = null
                window.decorView.systemUiVisibility = originalSystemUiVisibility
                requestedOrientation = originalOrientation
                customViewCallback?.onCustomViewHidden()
                customViewCallback = null
            }
        }

        webView.webChromeClient = webChromeClient
    }

    private fun configureWebClient() {
        mWebViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgress(true)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.e("Error", "onPageFinished")
                super.onPageFinished(view, url)
                if (!mIsError) {
                    if (url.contains("checkout/order-received")) {
                        showProgress(false)
                        setResult(Activity.RESULT_OK)
                        Toast.makeText(this@WebViewActivity, "Order placed successfully", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        showProgress(false)
                    }
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError
            ) {
                mIsError = true
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                Log.e("Error", "onReceivedHttpError")
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                Log.e("Error", "onReceivedSslError")
                super.onReceivedSslError(view, handler, error)
                Toast.makeText(this@WebViewActivity, "onReceivedSslError", Toast.LENGTH_LONG).show()
                finish()
            }

        }
        // Assign Web View Client to webview
        webView.webViewClient = mWebViewClient

    }

    private fun configureWebView() {
        try {
            webView.settings.apply {
                javaScriptCanOpenWindowsAutomatically = true
                javaScriptEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                databaseEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                webView.isFocusable = true
                webView.isFocusableInTouchMode = true
                webView.isScrollContainer = false
                webView.isVerticalScrollBarEnabled = false
                webView.isHorizontalScrollBarEnabled = false
                setAppCacheEnabled(false)
                setGeolocationEnabled(true)
                setAppCachePath(applicationContext.filesDir.absolutePath + "/cache")
                cacheMode = WebSettings.LOAD_NO_CACHE
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                loadsImagesAutomatically = true
                loadWithOverviewMode = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                setSupportMultipleWindows(false)
                builtInZoomControls = false
                setSupportZoom(false)
                userAgentString = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.0.0 Mobile Safari/537.36"
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        webView.clearHistory()
        webView.clearCache(true)
    }

}
