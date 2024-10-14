package uz.iskandarbek.solarsystem


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView

class CustomWebView(context: Context, attrs: AttributeSet?) : WebView(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Har qanday touch voqealarini o'tkazish, lekin skroll va zoomni o'chirish
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Faqat teginish voqeasini o'tkazish
                super.onTouchEvent(event)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                // Skrollingni o'chirish
                false
            }
            MotionEvent.ACTION_UP -> {
                super.onTouchEvent(event)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        // Skrollingni to'liq bloklash
        // Bu metodni o'zgartirish shart emas
    }
}