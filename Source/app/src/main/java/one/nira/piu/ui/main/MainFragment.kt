package one.nira.piu.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import one.nira.piu.R
import one.nira.piu.data.Const
import one.nira.piu.webview.BaseWebChromeClient
import one.nira.piu.webview.BaseWebViewClient
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.net.URLDecoder

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.main_fragment, container, false)
        val refresh: SwipeRefreshLayout = v.findViewById(R.id.fragmentRefresh)
        val webView: WebView = v.findViewById(R.id.webview)

        // 웹뷰 설정
        webView.webViewClient = BaseWebViewClient()
        webView.webChromeClient = BaseWebChromeClient(this.requireContext())
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.setDownloadListener { url, agent, contentDisp, mime, length ->
            val dnString: String = url.split("data:text/plain;charset=utf-8,")[1]
            val externalDir: String = this.requireContext().getExternalFilesDir(null)!!.absolutePath

            val dir = File(externalDir)
            val file = File(externalDir+"/"+Const.filename)
            if(!file.exists()) {
                dir.mkdirs()
                file.createNewFile()
            }
            val bw = BufferedWriter(FileWriter(file))
            bw.write(dnString)
            bw.close()

            val dlgBuilder: AlertDialog.Builder = AlertDialog.Builder(webView.context)
            dlgBuilder.setTitle(R.string.txt_dlg_save_title)
            dlgBuilder.setMessage(R.string.txt_dlg_save_cont)
            dlgBuilder.setNeutralButton(R.string.btn_txt_ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
            dlgBuilder.create().show()
        }

        webView.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                webView.goBack()
                true
            }
            false
        })

        @RequiresApi(Build.VERSION_CODES.O)
        webView.settings.safeBrowsingEnabled = true

        // 리프레시 설정
        refresh.setOnRefreshListener {
            webView.reload()
            refresh.isRefreshing = false
        }

        webView.clearCache(true)
        webView.loadUrl(Const.mainUrl)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}