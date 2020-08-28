package one.nira.piu.webview

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import one.nira.piu.R
import one.nira.piu.data.Const
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.net.URLDecoder
import java.net.URLEncoder

class BaseWebChromeClient(private val ctx: Context?): WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        val externalDir: String = ctx?.getExternalFilesDir(null)!!.absolutePath

        val file = File(externalDir+"/"+ Const.filename)

        if(!file.exists()) {
            // 파일 열지 못함 메시지
            val dialog = AlertDialog.Builder(ctx)
            dialog.setTitle(R.string.txt_dlg_fileop_title)
            dialog.setMessage(R.string.txt_dlg_fileop_notexist)
            dialog.setNeutralButton(R.string.btn_txt_ok,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
            dialog.create().show()
        }
        else {
            // 파일 열기
            val br = BufferedReader(FileReader(file))
            val s = br.readLine()
            br.close()

            webView?.loadUrl("javascript:callbackOpen('"+ s+"')")
        }

        return true
    }
}