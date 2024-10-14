package uz.iskandarbek.solarsystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var customWebView: CustomWebView
    private var dialog: AlertDialog? = null // dialog o'zgaruvchisini null bilan boshlaymiz
    private lateinit var networkReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customWebView = findViewById(R.id.customWebView)

        // WebView sozlamalari
        val webSettings: WebSettings = customWebView.settings
        webSettings.javaScriptEnabled = true  // Agar kerak bo'lsa
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.setSupportZoom(false)     // Zoomni to'liq o'chirish

        // Statik mashtabni belgilash (masalan, 120%)
        customWebView.setInitialScale(110)    // O'zingiz xohlagan mashtabni o'rnatish

        // Local fayllarni yuklash
        customWebView.webViewClient = WebViewClient()
        customWebView.loadUrl("file:///android_asset/index.html")  // Sayyoralarni tanlash sahifasi

        // Internet ulanishini tekshirish
        if (!isNetworkAvailable()) {
            showNoInternetDialog()
        }

        // BroadcastReceiver internet aloqasini tekshirish uchun
        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (isNetworkAvailable()) {
                    dialog?.dismiss() // Internet ulanishi bor, dialogni yopamiz
                } else {
                    showNoInternetDialog() // Internet ulanishi yo'q, dialogni ko'rsatamiz
                }
            }
        }
    }

    // Internet ulanishini tekshirish
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

    // Internet yo'qligini bildiruvchi dialog ko'rsatish
    private fun showNoInternetDialog() {
        if (dialog == null) { // Agar dialog hali o'rnatilmagan bo'lsa
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Internet ulanishi yo'q. Iltimos, internetni ulang.")
                .setCancelable(false) // Hech qanday tugma bo'lmasligi uchun
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() } // Tugmasiz bo'lishi uchun `null` qoldiramiz
            dialog = builder.create()
            dialog?.show() // Dialogni ko'rsatish
        }
    }

    override fun onStart() {
        super.onStart()
        // BroadcastReceiver'ni ro'yxatdan o'tkazish
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        // BroadcastReceiver'ni ro'yxatdan chiqarish
        unregisterReceiver(networkReceiver)
        dialog?.dismiss() // Aktivatsiya to'xtatilganda dialogni yopamiz
    }
}