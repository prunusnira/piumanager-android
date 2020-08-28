package one.nira.piu.webview

import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.Toast
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import one.nira.piu.R
import one.nira.piu.data.Const

class BaseWebViewClient: WebViewClientCompat() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if(request.url.host!! == Const.mainHost) {
            return false
        }

        Toast.makeText(
            view.context,
            view.context.getText(R.string.txt_client_urlwarn),
            Toast.LENGTH_SHORT
        ).show()
        return true
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
        Toast.makeText(view?.context, "SSL Error", Toast.LENGTH_SHORT).show()
        handler?.proceed()
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceErrorCompat
    ) {
        super.onReceivedError(view, request, error)
        Toast.makeText(view.context, error.errorCode.toString()+": "+error.description, Toast.LENGTH_SHORT).show()
    }
}